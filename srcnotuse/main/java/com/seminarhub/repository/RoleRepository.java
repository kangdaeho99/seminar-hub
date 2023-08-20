package com.seminarhub.repository;

import com.seminarhub.entity.Role;
import com.seminarhub.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT COUNT(*) FROM Role r WHERE r.role_type = :role_type AND del_dt is null")
    Long countByRole_type(@Param("role_type") RoleType role_type);

    @Query("SELECT r FROM Role r WHERE r.role_type = :role_type AND del_dt is null")
    Optional<Role> findByRole_type(@Param("role_type") RoleType role_type);
}
