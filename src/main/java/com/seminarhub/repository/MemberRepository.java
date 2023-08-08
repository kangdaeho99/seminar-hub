package com.seminarhub.repository;

import com.seminarhub.dto.MemberDTO;
import com.seminarhub.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * [ 2023-06-27 daeho.kang ]
     * Description : EntityGraph makes JOIN QUERY
     * It Shows the All Information of Member
     */
    @EntityGraph(attributePaths = {"member_role_set.role"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m From Member m WHERE m.member_id = :member_id AND del_dt is null")
    Optional<Member> findByMember_id(String member_id);

    @Query("SELECT COUNT(*) From Member where member_id = :member_id AND del_dt is null")
    Long countByMember_id(String member_id);

    @Query("UPDATE Member SET del_dt = CURRENT_TIMESTAMP WHERE member_id = :member_id")
    void deleteByMember_id(String member_id);

}
