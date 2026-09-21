package com.seminarhub.domain.member.repository;
import com.seminarhub.domain.member.domain.Role;
import com.seminarhub.domain.member.repository.querydsl.RoleRepositoryCustom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoleRepository extends JpaRepository<Role, Long>, RoleRepositoryCustom {
    Optional<Role> findByIdAndDeletedAtIsNull(Long id);
    List<Role> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}
