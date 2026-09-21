package com.seminarhub.domain.payment.service;
import com.seminarhub.domain.payment.domain.Payment;
import com.seminarhub.domain.payment.repository.PaymentRepository;
import com.seminarhub.global.dto.CursorRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository repository;
    @Transactional public Payment save(Payment e) { return repository.save(e); }
    public Optional<Payment> findById(Long id) { return repository.findByIdAndDeletedAtIsNull(id); }
    public List<Payment> findByIds(List<Long> ids) { return repository.findAllByIdInAndDeletedAtIsNull(ids); }
    public List<Payment> findAll(PaymentSearchQuery q) { return repository.search(q); }
    public Page<Payment> findAll(PaymentSearchQuery q, Pageable p) { return repository.search(q, p); }
    public List<Payment> findByCursor(PaymentSearchQuery q, CursorRequest c) { return repository.search(q, c); }
    @Transactional public Payment update(Payment e, String email, BigDecimal amount) { e.update(email, amount); return e; }
    @Transactional public Payment delete(Payment e) { e.delete(); return e; }
    @Transactional public List<Payment> deleteAll(List<Payment> es) { es.forEach(Payment::delete); return es; }
}
