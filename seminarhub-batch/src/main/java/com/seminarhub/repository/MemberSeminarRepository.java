package com.seminarhub.repository;

import com.seminarhub.entity.MemberSeminar;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 배치 모듈용 MemberSeminarRepository.
 * 주 용도: 통합 테스트에서 Delivery 더미 데이터 생성 시 연관 MemberSeminar 저장에 사용.
 */
public interface MemberSeminarRepository extends JpaRepository<MemberSeminar, Long> {
}
