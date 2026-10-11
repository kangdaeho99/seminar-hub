package com.seminarhub.domain.seminar.service;

import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.domain.seminar.domain.MemberSeminarItemStatusHistory;
import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.repository.MemberSeminarItemStatusHistoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSeminarItemStatusHistoryService {

    private final MemberSeminarItemStatusHistoryRepository repository;

    @Transactional(propagation = Propagation.MANDATORY)
    public MemberSeminarItemStatusHistory record(
            MemberSeminarItem memberSeminarItem,
            MemberSeminarItemStatus toStatus
    ) {
        return repository.save(
                new MemberSeminarItemStatusHistory(memberSeminarItem, toStatus)
        );
    }

    public Optional<MemberSeminarItemStatusHistory> findById(Long id) {
        return repository.findByIdAndDeletedAtIsNull(id);
    }

    public List<MemberSeminarItemStatusHistory> findByMemberSeminarItemId(Long memberSeminarItemId) {
        return repository.findAllByMemberSeminarItem_IdAndDeletedAtIsNullOrderByIdAsc(memberSeminarItemId);
    }
}
