package com.seminarhub.domain.seminar.repository;

import com.seminarhub.domain.seminar.domain.MemberSeminarItemStatusHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSeminarItemStatusHistoryRepository
        extends JpaRepository<MemberSeminarItemStatusHistory, Long> {

    Optional<MemberSeminarItemStatusHistory> findByIdAndDeletedAtIsNull(Long id);

    List<MemberSeminarItemStatusHistory>
            findAllByMemberSeminarItem_IdAndDeletedAtIsNullOrderByIdAsc(Long memberSeminarItemId);
}
