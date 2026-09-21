package com.seminarhub.domain.member.repository;
import com.seminarhub.domain.member.domain.Member;
import com.seminarhub.domain.member.repository.querydsl.MemberRepositoryCustom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);
    List<Member> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}
