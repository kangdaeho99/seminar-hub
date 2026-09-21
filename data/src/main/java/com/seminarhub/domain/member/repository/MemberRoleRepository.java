package com.seminarhub.domain.member.repository;
import com.seminarhub.domain.member.domain.MemberRole;
import com.seminarhub.domain.member.repository.querydsl.MemberRoleRepositoryCustom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long>, MemberRoleRepositoryCustom {
    Optional<MemberRole> findByIdAndDeletedAtIsNull(Long id);
    List<MemberRole> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}
