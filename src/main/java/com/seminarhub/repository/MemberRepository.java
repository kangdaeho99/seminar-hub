package com.seminarhub.repository;

import com.seminarhub.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.del_dt = CURRENT_TIMESTAMP WHERE m.member_id = :member_id")
    void deleteByMember_id(String member_id);

}
