package com.seminarhub.domain.delivery.service;
import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.enums.DeliveryStatus;
import com.seminarhub.domain.delivery.repository.DeliveryRepository;
import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class DeliveryService {
    private final DeliveryRepository repository;
    @Transactional public Delivery save(Delivery e) { return repository.save(e); }
    public Optional<Delivery> findById(Long id) { return repository.findByIdAndDeletedAtIsNull(id); }
    public List<Delivery> findByIds(List<Long> ids) { return repository.findAllByIdInAndDeletedAtIsNull(ids); }
    public List<Delivery> findAll(DeliverySearchQuery q) { return repository.search(q); }
    public Page<Delivery> findAll(DeliverySearchQuery q, Pageable p) { return repository.search(q, p); }
    public List<Delivery> findByCursor(DeliverySearchQuery q, CursorRequest c) { return repository.search(q, c); }
    @Transactional public Delivery update(Delivery e, MemberSeminar ms, DeliveryStatus status, String tracking, String courier) { e.update(ms, status, tracking, courier); return e; }
    @Transactional public Delivery delete(Delivery e) { e.delete(); return e; }
    @Transactional public List<Delivery> deleteAll(List<Delivery> es) { es.forEach(Delivery::delete); return es; }
}
