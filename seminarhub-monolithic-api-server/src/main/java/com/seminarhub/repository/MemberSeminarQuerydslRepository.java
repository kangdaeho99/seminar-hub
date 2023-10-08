package com.seminarhub.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.dto.Member_SeminarDTO;
import com.seminarhub.entity.Member;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.QMember_Seminar;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberSeminarQuerydslRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    public List<Member_Seminar> getMemberSeminarWithEntityExample(long member_seminar_no, int pageNo){
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
        List<Member_Seminar> member_seminarList = queryFactory
                .selectFrom(member_seminar)
                .where(member_seminar.member_seminar_no.eq(member_seminar_no))
                .offset(pageNo)
                .limit(10)
                .fetch();
        return member_seminarList;
    }
    public List<Member_SeminarDTO> getMemberSeminarWithDTOExample(long member_seminar_no, int pageNo){
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
        List<Member_SeminarDTO> member_seminarDTOList = queryFactory
                .select(Projections.fields(Member_SeminarDTO.class,
                        Expressions.asNumber(member_seminar_no).as("member_seminar_no"),
                        member_seminar.member.member_no
                        ))
                .from(member_seminar)
                .where(member_seminar.member_seminar_no.eq(member_seminar_no))
                .offset(pageNo)
                .limit(10)
                .fetch();
        return member_seminarDTOList;
    }

    public List<Member_SeminarDTO> getMemberSeminarWithDTOANDSelectEntityExample(long member_seminar_no){
        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
        List<Member_SeminarDTO> member_seminarDTOList = queryFactory
                .select(Projections.fields(Member_SeminarDTO.class,
                        member_seminar.member_seminar_no,
                        member_seminar.member.member_no.as("member_no")))
                .from(member_seminar)
                .where(member_seminar.member_seminar_no.eq(member_seminar_no))
                .groupBy(member_seminar.member_seminar_no)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();
        System.out.println(member_seminarDTOList);
        return member_seminarDTOList;

    }

//    public List<Member_SeminarDTO> getMemberSeminarWithCoveringIndex(Long member_seminar_no){
//        QMember_Seminar member_seminar = QMember_Seminar.member_Seminar;
//        List<Long> ids = queryFactory
//                .select()
//    }
    public void jdbcBulkInsert(List<Member_SeminarDTO> member_seminar_list){
        String sql = "insert into member_seminar (member_no, seminar_no) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql,
                member_seminar_list,
                member_seminar_list.size(),
                (PreparedStatement ps, Member_SeminarDTO member_seminarDTO) ->{
                    ps.setLong(1, member_seminarDTO.getMember_no());
                    ps.setLong(2, member_seminarDTO.getSeminar_no());
                });
    }



}
