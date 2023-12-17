package com.seminarhub.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.dto.SeminarPageResultDTO;
import com.seminarhub.dto.Seminar_Member_Seminar_PaymentResponseDTO;
import com.seminarhub.entity.*;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Repository
public class SeminarQuerydslRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    private final SeminarRepository seminarRepository;

    private final MemberRepository memberRepository;

    private final Member_SeminarRepository member_SeminarRepository;

    private final PaymentRepository paymentRepository;

    public List<Seminar> findByName(String seminar_name){
        QSeminar qSeminar = QSeminar.seminar;

        return queryFactory
                .selectFrom(qSeminar)
                .where(qSeminar.seminar_name.eq(seminar_name)
                .and(qSeminar.del_dt.isNull()))
                .fetch();
    }


    //Boolean Builder
    public List<Seminar> findSeminarByBooleanBuilder(String seminar_name, String seminar_explanation){
        BooleanBuilder builder = new BooleanBuilder();

        //어떤 쿼리인지 예상하기 어렵다.
        if(!StringUtils.isEmpty(seminar_name)){
            builder.and(QSeminar.seminar.seminar_name.eq(seminar_name));
        }
        if(!StringUtils.isEmpty(seminar_explanation)){
            builder.and(QSeminar.seminar.seminar_explanation.eq(seminar_explanation));
        }
        return queryFactory
                .selectFrom(QSeminar.seminar)
                .where(builder)
                .fetch();
    }

    //동적쿼리는 Boolean Expression : null 반환시 자등으로 조건절에서 제거된다. 모든 조건이 null이 발생하는 경우는 대장애 발생하니 주의!. 조건문이 다 삭제되기 떄문이다.
    public List<Seminar> findSeminarByBooleanExpression(String seminar_name, String seminar_explanation){
        return queryFactory
                .selectFrom(QSeminar.seminar)
                .where(eqName(seminar_name),
                        eqSeminarExplanation(seminar_explanation))
                .fetch();
    }

    private BooleanExpression eqName(String seminar_name){
        if(StringUtils.isEmpty(seminar_name)){
            return null;
        }
        return QSeminar.seminar.seminar_name.eq(seminar_name);
    }

    private BooleanExpression eqSeminarExplanation(String seminar_explanation){
        if(StringUtils.isEmpty(seminar_explanation)){
            return null;
        }
        return QSeminar.seminar.seminar_explanation.eq(seminar_explanation);
    }

    @Transactional
    public Boolean exist(String seminar_name){
        QSeminar qSeminar = QSeminar.seminar;
        Integer fetchOne = queryFactory
                .selectOne()
                .from(qSeminar)
                .where(qSeminar.seminar_name.eq(seminar_name))
                .fetchFirst();
        //limit 1로 조회 제한
        //fetchFirst == limit(1).fetchOne() 과 같다.
        //조회결과가 없으면 null이라서 체크
        return fetchOne != null; //0보다 크다 아니다가아닌 조회결과가 없으면 null이 반환된다.
    }

    //crossJoin 을 회피하자.
    //일반적으로 join을 맺을떄 CrossJoin이 성능이 잘 나오지 않는다.
    //crossjoin은 나올 수 있는 모든경우의수에 대해 나오기에 그렇다.
    // 묵시적 join으로 Cross join 발생(일부의 DB는 이에대해 어느정도 최적화가 지원된다.)
    // where
    public List<Member_Seminar> crossJoin(){
        QSeminar qSeminar = QSeminar.seminar;
        QMember_Seminar qMember_seminar = QMember_Seminar.member_Seminar;
        return queryFactory
                .selectFrom(qMember_seminar)
                .innerJoin(qSeminar)
                .on(qMember_seminar.seminar.seminar_no.eq(qSeminar.seminar_no))
                .fetch();
    }

    public List<Member_Seminar> getMember_Seminar(int member_seminar_no){
        QMember_Seminar qMember_Seminar = QMember_Seminar.member_Seminar;
        return queryFactory
                .select(Projections.fields(Member_Seminar.class,
                        qMember_Seminar.member,
                        qMember_Seminar.seminar))
                .from(qMember_Seminar)
                .limit(10)
                .fetch();
    }

    @Transactional
    public void bulkUpdatePrice() {
        QSeminar seminar = QSeminar.seminar;
        Random random = new Random();
        long[] arr = {10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000, 5000000};

        for(int i=2000000; i<2000025;i++){
//            System.out.println(random.nextInt(arr.length + 1) % arr.length);
            queryFactory.update(seminar)
            .set(seminar.seminar_price, arr[random.nextInt(12)%arr.length])
            .where(seminar.seminar_no.eq((long) i))
            .execute();
        }

    }

    @Transactional
    public void bulkUpdateSeminar_nameSeminar_explanation(String[] seminar_name, String[] seminar_explanation) {
        QSeminar seminar = QSeminar.seminar;
        Random random = new Random();

        System.out.println(seminar_explanation.length);
        for(int i=0; i<=2000000;i++){
//            System.out.println(random.nextInt(arr.length + 1) % arr.length);
            queryFactory.update(seminar)
            .set(seminar.seminar_name, seminar_name[random.nextInt(seminar_name.length + 1)%seminar_name.length])
            .set(seminar.seminar_explanation, seminar_explanation[random.nextInt(seminar_explanation.length + 1)%seminar_name.length])
            .where(seminar.seminar_no.eq((long) i))
            .execute();
        }

    }

    public void jdbcBulkInsert(List<SeminarDTO> seminar_list){
        String sql = "insert into seminar (seminar_name, seminar_explanation, seminar_price, seminar_max_participants, inst_dt, updt_dt ) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql,
                seminar_list,
                seminar_list.size(),
                (PreparedStatement ps, SeminarDTO dto) ->{
                    ps.setString(1, dto.getSeminar_name());
                    ps.setString(2, dto.getSeminar_explanation());
                    ps.setLong(3, dto.getSeminar_price());
                    ps.setLong(4, dto.getSeminar_max_participants());
                    ps.setTimestamp(5, dto.getUpdt_dt());
                    ps.setTimestamp(6,  dto.getInst_dt());
                });
    }


    public List<SeminarPageResultDTO> pagingSeminarWithKeyword(String keyword, int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        List<SeminarPageResultDTO> seminarPageResultDTOList = queryFactory
                .select(Projections.fields(SeminarPageResultDTO.class,
                        seminar.seminar_no,
                        seminar.seminar_name,
                        seminar.seminar_explanation,
                        seminar.seminar_price
                ))
                .from(seminar)
                .where(seminar.seminar_name.like(keyword + "%"))
                .orderBy(seminar.seminar_no.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();
        return seminarPageResultDTOList;
    }

    public List<SeminarPageResultDTO> pagingSeminarWithKeywordWithCoveringIndex(String keyword, int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        List<Long> ids = queryFactory
                .select(seminar.seminar_no)
                .from(seminar)
                .where(seminar.seminar_name.like(keyword + '%'))
                .orderBy(seminar.seminar_name.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();

        if(CollectionUtils.isEmpty(ids)){
            return new ArrayList<>();
        }

        List<SeminarPageResultDTO> seminarPageResultDTOList = queryFactory
                .select(Projections.fields(SeminarPageResultDTO.class,
                        seminar.seminar_no,
                        seminar.seminar_name,
                        seminar.seminar_explanation,
                        seminar.seminar_price
                ))
                .from(seminar)
                .where(seminar.seminar_no.in(ids))
                .orderBy(seminar.seminar_no.desc())
                .fetch();
        return seminarPageResultDTOList;
    }

    public List<SeminarPageResultDTO> pagingSeminarWithSeminar_Price(long seminar_price, int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        List<SeminarPageResultDTO> seminarPageResultDTOList = queryFactory
                .select(Projections.fields(SeminarPageResultDTO.class,
                        seminar.seminar_no,
                        seminar.seminar_name,
                        seminar.seminar_explanation,
                        seminar.seminar_price
                ))
                .from(seminar)
                .where(seminar.seminar_price.eq(seminar_price))
                .orderBy(seminar.seminar_no.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();
        return seminarPageResultDTOList;
    }

    public List<SeminarPageResultDTO> pagingSeminarWithCoveringIndexWithSeminar_Price(long seminar_price, int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        List<Long> ids = queryFactory
                .select(seminar.seminar_no)
                .from(seminar)
                .where(seminar.seminar_price.eq(seminar_price))
                .orderBy(seminar.seminar_no.asc())
                .limit(pageSize)
                .offset(pageNo*pageSize)
                .fetch();
        
        if(CollectionUtils.isEmpty(ids)){
            return new ArrayList<>();
        }

        List<SeminarPageResultDTO> seminarPageResultDTOList = queryFactory
                .select(Projections.fields(SeminarPageResultDTO.class,
                        seminar.seminar_no,
                        seminar.seminar_name,
                        seminar.seminar_explanation,
                        seminar.seminar_price
                ))
                .from(seminar)
                .where(seminar.seminar_no.in(ids))
                .orderBy(seminar.seminar_no.desc())
                .fetch();
        return seminarPageResultDTOList;
    }

    @Cacheable(value = "mainPageSeminarList",  key = "'page-' + #pageNo + '-limit-' + #limit")
    public List<SeminarPageResultDTO> mainPagePagingSeminarWithEhCache(int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        List<SeminarPageResultDTO> seminarPageResultDTOList = queryFactory
                .select(Projections.fields(SeminarPageResultDTO.class,
                        seminar.seminar_no,
                        seminar.seminar_name,
                        seminar.seminar_explanation,
                        seminar.seminar_price
                ))
                .from(seminar)
                .orderBy(seminar.seminar_no.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();
        return seminarPageResultDTOList;
    }

    @Cacheable(value = "mainPageSeminarList",  key = "'page-' + #pageNo + '-limit-' + #limit")
    public List<SeminarPageResultDTO> mainPagePagingSeminarWithCoveringIndexAndEhCache(int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        List<Long> ids = queryFactory
                .select(seminar.seminar_no)
                .from(seminar)
                .orderBy(seminar.seminar_no.desc())
                .limit(pageSize)
                .offset(pageNo*pageSize)
                .fetch();

        if(CollectionUtils.isEmpty(ids)){
            return new ArrayList<>();
        }

        List<SeminarPageResultDTO> seminarPageResultDTOList = queryFactory
                .select(Projections.fields(SeminarPageResultDTO.class,
                        seminar.seminar_no,
                        seminar.seminar_name,
                        seminar.seminar_explanation,
                        seminar.seminar_price
                ))
                .from(seminar)
                .where(seminar.seminar_no.in(ids))
                .fetch();
        return seminarPageResultDTOList;
    }


    //syncrhonzied사용시 에러남. 한번 원인찾아서 써보기. 지금 seminar_participant_cnt에 안되는 이유는 해당 쿼리는 entity가 아니기 때문임.
    //신청쿼리 작성해보기
    @Transactional
    public void participateOnSeminar(Long member_no, Long seminar_no){
        QSeminar seminar = QSeminar.seminar;
        //신청시 인원이 몇명인지 확인하고 각 세미나의 maxParticipant가 넘는지 안넘는지 확인한다.
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;

        Long seminar_participnat_cnt = queryFactory.select(member_seminar.count())
                .from(member_seminar)
                .where(member_seminar.seminar.seminar_no.eq(seminar_no)
                        .and(member_seminar.del_dt.isNull()))
                .fetchOne();

        System.out.println("seminar_participant_cnt:"+seminar_participnat_cnt);

        Long maxParticipant_cnt = (long) 0;
        maxParticipant_cnt = queryFactory.select(seminar.seminar_maxParticipants)
                .from(seminar)
                .where(seminar.seminar_no.eq(seminar_no)
                        .and(seminar.del_dt.isNull()))
                .fetchOne();
        System.out.println("maxParticipant_cnt:"+maxParticipant_cnt);
        if(seminar_participnat_cnt < maxParticipant_cnt){ //인원이 더적다면 member_seminar에 넣는다.
            Optional<Member> memberEntity = memberRepository.findByMember_no(member_no);
            Optional<Seminar> seminarEntity = seminarRepository.findBySeminar_no(seminar_no);

            Member_Seminar member_seminarEntity = Member_Seminar.builder()
                            .member(memberEntity.get())
                            .seminar(seminarEntity.get())
                            .build();

            member_SeminarRepository.save(member_seminarEntity);
        }else{
            System.out.println("Max_Participant Over");
        }


    }

    @Transactional
    public void participateOnSeminarWithPESSIMISTICLock(Long member_no, Long seminar_no){
        QSeminar seminar = QSeminar.seminar;
        //신청시 인원이 몇명인지 확인하고 각 세미나의 maxParticipant가 넘는지 안넘는지 확인한다.
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;

        // 세미나 레코드를 PESSIMISTIC_WRITE 락으로 가져옵니다.
        Seminar seminarEntityLock = queryFactory.selectFrom(seminar)
                .where(seminar.seminar_no.eq(seminar_no)
                        .and(seminar.del_dt.isNull()))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 설정
                .fetchOne();

        if (seminarEntityLock != null) {
            Long seminar_participant_cnt = queryFactory.select(member_seminar.count())
                    .from(member_seminar)
                    .where(member_seminar.seminar.seminar_no.eq(seminar_no)
                            .and(member_seminar.del_dt.isNull()))
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .fetchOne();

            Long maxParticipant_cnt = seminarEntityLock.getSeminar_maxParticipants();
            System.out.println("seminar_participant_cnt: " + seminar_participant_cnt);
            System.out.println("maxParticipant_cnt: " + maxParticipant_cnt);
            if (seminar_participant_cnt < maxParticipant_cnt) {
                Optional<Member> memberEntity = memberRepository.findByMember_no(member_no);
                Optional<Seminar> seminarEntity = seminarRepository.findBySeminar_no(seminar_no);

                if (memberEntity.isPresent() && seminarEntity.isPresent()) {
                    Member_Seminar member_seminarEntity = Member_Seminar.builder()
                            .member(memberEntity.get())
                            .seminar(seminarEntity.get())
                            .build();

                    member_SeminarRepository.save(member_seminarEntity);
                }
            } else{
                System.out.println("bigger than MAX_Participant ");
            }
        }
    }

    @Transactional
    public void participateOnSeminarWithPESSIMISTICLockAddPayment(Long member_no, Long seminar_no){
        QSeminar seminar = QSeminar.seminar;
        //신청시 인원이 몇명인지 확인하고 각 세미나의 maxParticipant가 넘는지 안넘는지 확인한다.
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;

        // 세미나 레코드를 PESSIMISTIC_WRITE 락으로 가져옵니다.
        Seminar seminarEntityLock = queryFactory.selectFrom(seminar)
                .where(seminar.seminar_no.eq(seminar_no)
                        .and(seminar.del_dt.isNull()))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 설정
                .fetchOne();

        if (seminarEntityLock != null) {
            Long seminar_participant_cnt = queryFactory.select(member_seminar.count())
                    .from(member_seminar)
                    .where(member_seminar.seminar.seminar_no.eq(seminar_no)
                            .and(member_seminar.del_dt.isNull()))
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .fetchOne();

            Long maxParticipant_cnt = seminarEntityLock.getSeminar_maxParticipants();
            System.out.println("seminar_participant_cnt: " + seminar_participant_cnt);
            System.out.println("maxParticipant_cnt: " + maxParticipant_cnt);
            if (seminar_participant_cnt < maxParticipant_cnt) {
                Optional<Member> memberEntity = memberRepository.findByMember_no(member_no);
                Optional<Seminar> seminarEntity = seminarRepository.findBySeminar_no(seminar_no);

                if (memberEntity.isPresent() && seminarEntity.isPresent()) {
                    Member_Seminar member_seminarEntity = Member_Seminar.builder()
                            .member(memberEntity.get())
                            .seminar(seminarEntity.get())
                            .build();

                    Payment payment = Payment.builder()
                            .member_seminar(member_seminarEntity)
                            .amount(seminarEntity.get().getSeminar_price())
                            .build();

                    payment.setMember_seminar(member_seminarEntity);
                    paymentRepository.save(payment);
                    member_seminarEntity.setPayment(payment);
                    member_SeminarRepository.save(member_seminarEntity);


                }
            } else{
                System.out.println("bigger than MAX_Participant ");
            }
        }
    }


    /**
     * Service단 고려해서 진행할때.
     */
    public List<Seminar> getSeminar(long seminar_no){
        QSeminar seminar = QSeminar.seminar;
//        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
//        QPayment payment = QPayment.payment;

        //먼저 Seminar_no가 주어지면 해당 값에 대한 모든 Member_Seminar를 가져올것이다.
        List<Seminar> seminarEntity = queryFactory
                .select(seminar)
                .from(seminar)
//                .leftJoin(seminar.member_seminar_list, member_seminar)
//                .fetchJoin()
//                .leftJoin(member_seminar.payment, payment)
//                .fetchJoin()
                .where(seminar.seminar_no.eq(seminar_no))
                .fetch();

        return seminarEntity;
    }

    /**
     * Service단 고려해서 진행할때.
     */
    public List<Seminar> getSeminarFetchJoin(long seminar_no){
        QSeminar seminar = QSeminar.seminar;
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
        QPayment payment = QPayment.payment;

        //먼저 Seminar_no가 주어지면 해당 값에 대한 모든 Member_Seminar를 가져올것이다.
        List<Seminar> seminarEntity = queryFactory
                .select(seminar)
                .from(seminar)
//                .leftJoin(seminar.member_seminar_list, member_seminar)
                .fetchJoin()
                .leftJoin(member_seminar.payment, payment)
                .fetchJoin()
                .where(seminar.seminar_no.eq(seminar_no))
                .fetch();

        return seminarEntity;
    }

    /**
     * 이렇게하면 JPA N+1 문제는 발생하지만 문제는 없다!!!! 하지만 성능에 안좋겠지.
     * 이걸 우리는 해결해가야하는것이다~~
     * [  daeho.kang ]
     * Description :
     *
     */
    public List<Seminar> getListSeminar(int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
//        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
//        QPayment payment = QPayment.payment;

        //먼저 Seminar_no가 주어지면 해당 값에 대한 모든 Member_Seminar를 가져올것이다.
        List<Seminar> seminarEntity = queryFactory
                .select(seminar)
                .from(seminar)
//                .leftJoin(seminar.member_seminar_list, member_seminar)
//                .fetchJoin()
//                .leftJoin(member_seminar.payment, payment)
//                .fetchJoin()
//                .where(seminar.seminar_no.eq(seminar_no))
                .orderBy(seminar.seminar_no.desc())
                .limit(pageSize)
                .offset(pageNo)
                .fetch();
        return seminarEntity;
    }
    public List<Seminar> getListSeminarWithFetchJoin(Long seminar_no, int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
        QPayment payment = QPayment.payment;

        List<Seminar> seminarEntity = queryFactory
                .select(seminar)
                .from(seminar)
                .leftJoin(seminar.member_seminar_list, member_seminar)
//                .fetchJoin()
//                .leftJoin(member_seminar.payment, payment)
//                .fetchJoin()
//                .where(seminar.seminar_no.eq(seminar_no))
                .orderBy(seminar.seminar_no.desc())
                .offset(pageNo * pageSize)
                .limit(pageSize)
                .fetch();

        return seminarEntity;
    }
    public List<Seminar> getParticularListSeminarWithFetchJoin(Long seminar_no, int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
        QPayment payment = QPayment.payment;

        List<Seminar> seminarEntity = queryFactory
                .select(seminar)
                .from(seminar)
                .leftJoin(seminar.member_seminar_list, member_seminar)
                .fetchJoin()
//                .leftJoin(member_seminar.payment, payment)
//                .fetchJoin()
                .where(seminar.seminar_no.eq(seminar_no))
                .orderBy(seminar.seminar_no.desc())
                .offset(pageNo * pageSize)
                .limit(pageSize)
                .fetch();

        return seminarEntity;
    }

    public List<Seminar> getListSeminarWithBatch(Long seminar_no, int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
        QPayment payment = QPayment.payment;

        List<Seminar> seminarEntity = queryFactory
                .select(seminar)
                .from(seminar)
//                .leftJoin(seminar.member_seminar_list, member_seminar)
//                .fetchJoin()
//                .leftJoin(member_seminar.payment, payment)
//                .fetchJoin()
                .where(seminar.seminar_no.eq(seminar_no))
                .orderBy(seminar.seminar_no.desc())
                .offset(pageNo * pageSize)
                .limit(pageSize)
                .fetch();
        return seminarEntity;
    }



    /**
     *
     * 단순히 DTO로 값을 받는경우
     */
    public List<Seminar_Member_Seminar_PaymentResponseDTO> getListForMember_SeminarAndPayment(long seminar_no){
        QSeminar seminar = QSeminar.seminar;
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
        QPayment payment = QPayment.payment;

        //먼저 Seminar_no가 주어지면 해당 값에 대한 모든 Member_Seminar를 가져올것이다.
        List<Seminar_Member_Seminar_PaymentResponseDTO> list = queryFactory
                .select(Projections.fields(Seminar_Member_Seminar_PaymentResponseDTO.class,
                        seminar.seminar_no.as("seminar_no"),
                        seminar.seminar_name.as("seminar_name"),
                        seminar.seminar_explanation.as("seminar_explanation"),
                        seminar.seminar_price.as("seminar_price"),
                        member_seminar.member_seminar_no.as("member_seminar_no"),
                        member_seminar.member.member_no.as("member_no"),
                        payment.payment_no,
                        payment.amount))
                .from(seminar)
                .leftJoin(seminar.member_seminar_list, member_seminar)
                .leftJoin(member_seminar.payment, payment)
                .where(seminar.seminar_no.eq(seminar_no))
                .fetch();

        return list;
    }


    public List<SeminarPageResultDTO> pagingSeminarWithWholeword(String keyword, int pageNo, int pageSize){
        QSeminar seminar = QSeminar.seminar;
        List<SeminarPageResultDTO> seminarPageResultDTOList = queryFactory
                .select(Projections.fields(SeminarPageResultDTO.class,
                        seminar.seminar_no,
                        seminar.seminar_name,
                        seminar.seminar_explanation,
                        seminar.seminar_price
                ))
                .from(seminar)
                .where(seminar.seminar_name.like("%"+keyword + "%"))
                .orderBy(seminar.seminar_no.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();
        return seminarPageResultDTOList;
    }





}
