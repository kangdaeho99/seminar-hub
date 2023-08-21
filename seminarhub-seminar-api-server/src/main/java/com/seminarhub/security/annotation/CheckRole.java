package com.seminarhub.security.annotation;

import com.seminarhub.core.entity.RoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [ 2023-07-21 daeho.kang ]
 * Description : @CheckRole 어노테이션을 통해 ROLE_USER, ROLE_ADMIN, ROLE_MANAGER에 Controller 접속권한을 체크합니다.
 * RoleType[] 으로 선언하여 복수개의 권한 체크 가능합니다.
 * API 호출때마다 DataBase에 접속하여 DB에서 member의 Role을 체크하는것은 비효율적이라는 생각이 들어, 회원관리쪽은 JWT에 roles claims를 삽입하여 사용합니다.
 * 사용에시 :
 *     @CheckRole(roles = RoleType.USER) : RoleType.USER 의 권한을 가진 사용자만 접근가능합니다.
 *     @CheckRole(roles = {RoleType.USER, RoleType.ADMIN}) : RoleType.ADMIN 의 권한을 가진 사용자만 접근가능합니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {
//    RoleType role() default RoleType.USER;
    RoleType[] roles() default {RoleType.USER};

}
