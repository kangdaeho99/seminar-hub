package com.seminarhub.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.entity.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SeminarQuerydslRepository {
    private final JPAQueryFactory queryFactory;

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


}
