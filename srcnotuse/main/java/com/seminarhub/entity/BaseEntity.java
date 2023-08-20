package com.seminarhub.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * [ 2023-07-11 daeho.kang ]
 * Description :
 * @Desc Entity 데이터 등록 시간, 수정시간 자동으로 추가, 변경되게해주는 class
 *
 * @MappedSuperclass : 해당 어노테이션이 적용된 클래스는 테이블로 생성되지 않음,
 * 실제 테이블은 BaseEntity를 상속한 엔티티의 클래스로 데이터베이스 테이블이 생성됨
 *
 * @EntityListeners(value = {AuditingEntityListener.class}) : JPA 엔티티 객체들은 Persistence COntext에서 관리.
 * 해당 엔티티 객체들의 변화를 감지, AuditingEntityListener.class가 JPA 내부에서 엔티티 객체 생성/변경 되는것을 감지
 *
 * @CreatedDate : JPA에서 엔티티의 생성시간을 처리
 * @LastModifiedDate : 최종 수정시간을 자동으로 처리하는 용도,
 *
 * 속성으로 insertable updateable 등이 있는데 updatable = false 일경우 해당 엔티티 객체를 데이터베이스에 반영할때 regdate칼럼값은
 * 변경되지 않습니다.
 * JPA를 이용하면서 AuditingEntityListener를 활성화시키기 위해서는 프로젝트에 @EnableJpaAuditing 설정을 추가해아힙니다.
 * 프로젝트 생성시에 존재하는 Application @SpringBootApplication에 @EnableJpaAuditing 을 추가합니다.
 */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(name = "inst_dt")
    private LocalDateTime inst_dt;

    @LastModifiedDate
    @Column(name = "updt_dt")
    private LocalDateTime updt_dt;

}
