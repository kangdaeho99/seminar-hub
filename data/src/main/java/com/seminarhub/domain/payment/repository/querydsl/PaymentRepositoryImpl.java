package com.seminarhub.domain.payment.repository.querydsl;
import static com.seminarhub.domain.payment.domain.QPayment.payment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.domain.payment.domain.Payment;
import com.seminarhub.domain.payment.service.PaymentSearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.global.repository.QuerydslRepositorySupport;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
@Repository
public class PaymentRepositoryImpl extends QuerydslRepositorySupport<Payment> implements PaymentRepositoryCustom {
    public PaymentRepositoryImpl(JPAQueryFactory f) { super(f, payment, payment.id, payment.createdAt, payment.updatedAt, payment.deletedAt); }
    public List<Payment> search(PaymentSearchQuery q) { return findAll(condition(q)); }
    public Page<Payment> search(PaymentSearchQuery q, Pageable p) { return findAll(condition(q), p, this::sort); }
    public List<Payment> search(PaymentSearchQuery q, CursorRequest c) { return findByCursor(condition(q), c); }
    private BooleanBuilder condition(PaymentSearchQuery q) {
        BooleanBuilder b = auditCondition(q);
        if (StringUtils.hasText(q.email())) b.and(payment.email.containsIgnoreCase(q.email()));
        if (q.paymentAmount() != null) b.and(payment.paymentAmount.eq(q.paymentAmount()));
        if (q.paymentAmountMin() != null) b.and(payment.paymentAmount.goe(q.paymentAmountMin()));
        if (q.paymentAmountMax() != null) b.and(payment.paymentAmount.loe(q.paymentAmountMax()));
        return b;
    }
    private ComparableExpressionBase<?> sort(String n) { return switch (n) {
        case "id" -> payment.id; case "email" -> payment.email; case "paymentAmount" -> payment.paymentAmount;
        case "createdAt" -> payment.createdAt; case "updatedAt" -> payment.updatedAt;
        case "deletedAt" -> payment.deletedAt; default -> null; }; }
}
