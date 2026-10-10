package com.seminarhub.domain.seminar.service;

import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.domain.seminar.domain.Seminar;
import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.repository.MemberSeminarItemRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSeminarItemService {
    private final MemberSeminarItemRepository repository;

    @Transactional
    public MemberSeminarItem save(MemberSeminarItem item) {
        return repository.save(item);
    }

    public Optional<MemberSeminarItem> findById(Long id) {
        return repository.findByIdAndDeletedAtIsNull(id);
    }

    public List<MemberSeminarItem> findByMemberSeminarId(Long memberSeminarId) {
        return repository.findAllByMemberSeminar_IdAndDeletedAtIsNull(memberSeminarId);
    }

    public List<MemberSeminarItem> findBySeminarId(Long seminarId) {
        return repository.findAllBySeminar_IdAndDeletedAtIsNull(seminarId);
    }

    @Transactional
    public MemberSeminarItem update(MemberSeminarItem item, MemberSeminar memberSeminar, Seminar seminar) {
        item.update(memberSeminar, seminar);
        return item;
    }

    @Transactional
    public MemberSeminarItem changeStatus(MemberSeminarItem item, MemberSeminarItemStatus status) {
        item.changeStatus(status);
        return item;
    }

    @Transactional
    public MemberSeminarItem delete(MemberSeminarItem item) {
        item.delete();
        return item;
    }
}
