package com.seminarhub.repository;


import com.seminarhub.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * [ 2023-08-30 daeho.kang ]
 * Description: Repository interface for accessing Member entities using JPA.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * [ 2023-06-27 daeho.kang ]
     * Description: Retrieves a Member entity by member_ID while also fetching associated roles using JOIN QUERY.
     * The result is customized using an EntityGraph to eagerly load the role information.
     *
     */
    @EntityGraph(attributePaths = {"member_role_set.role"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m From Member m WHERE m.member_id = :member_id AND del_dt is null")
    Optional<Member> findByMember_id(@Param("member_id") String member_id);

    @EntityGraph(attributePaths = {"member_role_set.role"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m From Member m WHERE m.member_no = :member_no AND del_dt is null")
    Optional<Member> findByMember_no(@Param("member_no") Long member_no);

}
