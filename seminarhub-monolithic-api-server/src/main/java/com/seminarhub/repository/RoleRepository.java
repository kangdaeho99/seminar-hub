package com.seminarhub.repository;

import com.seminarhub.entity.Role;
import com.seminarhub.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * [2023-08-30 daeho.kang]
 * Description: Repository interface for interacting with Role entities using JPA.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * [2023-08-30 daeho.kang]
     * Description: Counts the number of Role entities with the specified role type that are not marked as deleted.
     *
     */
    @Query("SELECT COUNT(*) FROM Role r WHERE r.role_type = :role_type AND del_dt is null")
    Long countByRole_type(@Param("role_type") RoleType role_type);

    /**
     * [2023-08-30 daeho.kang]
     * Description: Retrieves a Role entity based on the specified role type that is not marked as deleted.
     *
     */
    @Query("SELECT r FROM Role r WHERE r.role_type = :role_type AND del_dt is null")
    Optional<Role> findByRole_type(@Param("role_type") RoleType role_type);
}
