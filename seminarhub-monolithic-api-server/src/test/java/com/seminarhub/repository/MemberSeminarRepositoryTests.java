package com.seminarhub.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.dto.Member_SeminarDTO;
import com.seminarhub.entity.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;


/**
 * [ 2023-08-08 daeho.kang ]
 * Description : Test For QueryDSL Or JPQL Custom Method
 *
 */
@SpringBootTest
public class MemberSeminarRepositoryTests {
    @Autowired
    private Member_SeminarRepository memberSeminarRepository;

    @Autowired
    private Member_SeminarQuerydslRepository memberSeminarQuerydslRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SeminarRepository seminarRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    private final Long member_seminar_no = 1L;
    private final String member_id = "daeho.kang@naver.com"+ UUID.randomUUID();

    private final String member_password = "123123123";
    private final String seminar_name= "SeminarTest" + UUID.randomUUID();

    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : memberSeminarRepository findBySeminar_name Test
     * Find Seminar by seminar_name
     *
     *     select
     *         m1_0.member_seminar_no,
     *         m1_0.del_dt,
     *         m1_0.inst_dt,
     *         m1_0.member_id,
     *         m1_0.seminar_name,
     *         m1_0.updt_dt
     *     from
     *         member_seminar m1_0
     *     where
     *         m1_0.member_seminar_no=?
     *         and m1_0.del_dt is null
     */
    @DisplayName("findByMember_Seminar_no Test")
    @Test
    public void testGetWithMember_Seminar_no(){
        // given, when, then
        Optional<Member_Seminar> member_seminar = memberSeminarRepository.findByMember_Seminar_no(member_seminar_no);

        // then
//        assertNotNull(member_seminar.get());
//        System.out.println(member_seminar.get());
    }


    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : memberSeminarRepository findBySeminar_name Test
     * Find Seminar by seminar_name
     *
     *     select
     *         s1_0.seminar_no,
     *         s1_0.del_dt,
     *         s1_0.inst_dt,
     *         s1_0.seminar_name,
     *         s1_0.updt_dt
     *     from
     *         seminar s1_0
     *     where
     *         s1_0.seminar_name=?
     *         and s1_0.del_dt is null
     *         Member_Seminar(member_seminar_no=17, del_dt=null)
     */
    @DisplayName("findAllByMember_id Test")
    @Test
    public void testGetWithMember_id(){
        // given, when
        List<Member_Seminar> member_seminar = memberSeminarRepository.findAllByMember_id(member_id);

        // then
        assertNotNull(member_seminar);
        member_seminar.stream().forEach(System.out::println);
    }

    /**
     * [ 2023-08-08 daeho.kang ]
     * Description : memberSeminarRepository findBySeminar_name Test
     * Find Seminar by seminar_name
     *
     *     select
     *         m1_0.member_seminar_no,
     *         m1_0.del_dt,
     *         m1_0.inst_dt,
     *         m1_0.member_id,
     *         m1_0.seminar_name,
     *         m1_0.updt_dt
     *     from
     *         member_seminar m1_0
     *     join
     *         seminar s1_0
     *             on s1_0.seminar_no=m1_0.seminar_name
     *     where
     *         s1_0.seminar_name=?
     *         and m1_0.del_dt is null
     *     Member_Seminar(member_seminar_no=16, del_dt=null)
     */
    @DisplayName("findAllBySeminar_name Test")
    @Test
    public void testGetWithSeminar_name(){
        // given, when
        List<Member_Seminar> member_seminar = memberSeminarRepository.findAllBySeminar_name(seminar_name);

        // then
        assertNotNull(member_seminar);
        member_seminar.stream().forEach(System.out::println);
    }

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    asveAll사용시 hibernate에서 모든것 save하기전에 select를 한다.
    @DisplayName("dummyInsertWithJPAHibernateSaveAll")
    @Test
    public void dummyInsertWithJPAHibernateSaveAll(){
        List<Member_Seminar> member_seminarList = new LinkedList<>();
        Optional<Seminar> seminar = seminarRepository.findBySeminar_name("SeminarDummyIndex"+0);;
        for(int i=0;i<100; i++){
            Optional<Member> member = memberRepository.findByMember_id("MemberDummyIndex"+i);
            int cnt=0;
            for(int j=0;j<10000;j++){
                if(j == 0){
                    seminar = seminarRepository.findBySeminar_name("SeminarDummyIndex"+j);
                }
                Member_Seminar member_seminar = Member_Seminar.builder()
                        .member(member.get())
                        .seminar(seminar.get())
                        .build();
                member_seminarList.add(member_seminar);
            }
        }
        Long startTime = System.currentTimeMillis();
        memberSeminarRepository.saveAll(member_seminarList);
        Long endTime = System.currentTimeMillis();
        System.out.println("Execution Time:"+ (endTime - startTime) + "ms");
    }
    @DisplayName("dummyInsertWithJdbcTemplate")
    @Test
    public void dummyInsertWithJdbcTemplate(){
        List<Member_SeminarDTO> member_seminarDTOList = new LinkedList<>();
        Random random = new Random();
        long randomMemberNo = 0;
        long randomSeminarNo = 0;
        for(int i=0;i<100; i++){
            for(int j=0;j<100;j++){
                randomMemberNo = random.nextInt(120) + 1; // 1부터 99999까지의 범위
                randomSeminarNo = random.nextInt(999999) + 1; // 1부터 99999까지의 범위

                Member_SeminarDTO memberSeminarDTO = Member_SeminarDTO.builder()
                        .member_no((long) randomMemberNo)
                        .seminar_no((long) randomSeminarNo)
                        .build();
                member_seminarDTOList.add(memberSeminarDTO);
            }
        }
        Long startTime = System.currentTimeMillis();
        memberSeminarQuerydslRepository.jdbcBulkInsert(member_seminarDTOList);
        Long endTime = System.currentTimeMillis();
        System.out.println("Execution Time:"+ (endTime - startTime) + "ms");

    }

    @DisplayName("QueryDsl getMemberSeminarWithEntityExample Test")
    @Test
    public void testgetMemberSeminarWithEntityExample(){
        System.out.println(memberSeminarQuerydslRepository.getMemberSeminarWithEntityExample(2,0));
    }

    @DisplayName("QueryDsl getMemberSeminarWithDTOExample With AS Test")
    @Test
    public void testgetMemberSeminarWithDTOExample(){
        System.out.println(memberSeminarQuerydslRepository.getMemberSeminarWithDTOExample(2,0));
    }

    @DisplayName("QueryDsl getMemberSeminarWithDTOANDSelectEntityExample With AS Test")
    @Test
    public void testgetMemberSeminarWithDTOANDSelectEntityExample(){
//        System.out.println(memberSeminarQuerydslRepository.getMemberSeminarWithDTOANDSelectEntityExample(2));
        List<Member_Seminar> member_seminarList = memberSeminarQuerydslRepository.getMemberSeminarWithDTOANDSelectEntityExample(2).stream().map(memberSeminarDTO -> memberSeminarDTO.dtoToEntity()).collect(Collectors.toList());
        System.out.println(member_seminarList.get(0));
        System.out.println(member_seminarList.get(0).getMember().getMember_no());
    }

    @Transactional
    @DisplayName("getMember_SeminarBySeminar_noPagination test")
    @Test
    public void getMember_SeminarBySeminar_noPagination(){
        Long member_no = 1L;
        Long seminar_no = 2000028L;
        int pageNo = 0;
        int pageSize = 10;
        List<Member_Seminar> list = memberSeminarQuerydslRepository.getMember_SeminarBySeminar_noPagination(seminar_no, pageNo, pageSize);
        list.stream().forEach(list2 -> System.out.println(list2));

    }

}