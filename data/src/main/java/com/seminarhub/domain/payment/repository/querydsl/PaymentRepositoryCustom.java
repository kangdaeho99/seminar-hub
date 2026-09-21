package com.seminarhub.domain.payment.repository.querydsl;
import com.seminarhub.domain.payment.domain.Payment;
import com.seminarhub.domain.payment.service.PaymentSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface PaymentRepositoryCustom {
    List<Payment> search(PaymentSearchQuery query);
    Page<Payment> search(PaymentSearchQuery query, Pageable pageable);
    List<Payment> search(PaymentSearchQuery query, CursorRequest cursorRequest);
}
