package com.seminarhub.domain.seminar.repository;

import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSeminarItemRepository extends JpaRepository<MemberSeminarItem, Long> {
    Optional<MemberSeminarItem> findByIdAndDeletedAtIsNull(Long id);

    List<MemberSeminarItem> findAllByMemberSeminar_IdAndDeletedAtIsNull(Long memberSeminarId);

    List<MemberSeminarItem> findAllBySeminar_IdAndDeletedAtIsNull(Long seminarId);
}
