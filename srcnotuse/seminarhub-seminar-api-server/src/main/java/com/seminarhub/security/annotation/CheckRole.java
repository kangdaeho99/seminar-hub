package com.seminarhub.security.annotation;

import com.seminarhub.entity.RoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [ 2023-07-21 daeho.kang ]
 * Description: Custom annotation used to check controller access rights based on roles.
 * The @CheckRole annotation is used to enforce access control for ROLE_USER, ROLE_ADMIN, and ROLE_MANAGER.
 * It allows checking multiple roles by specifying an array of RoleType values.
 * To avoid inefficient database queries for member roles on every API call,
 * member management utilizes JWT with roles claims embedded for authentication.
 *
 * Example usage:
 * - @CheckRole(roles = RoleType.USER): Only users with RoleType.USER authority can access the annotated endpoint.
 * - @CheckRole(roles = {RoleType.USER, RoleType.ADMIN}): Only users with RoleType.USER or RoleType.ADMIN authority can access the annotated endpoint.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {
//    RoleType role() default RoleType.USER;
    RoleType[] roles() default {RoleType.USER};

}
