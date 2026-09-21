package com.seminarhub.domain.delivery.repository.querydsl;
import static com.seminarhub.domain.delivery.domain.QDelivery.delivery;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.service.DeliverySearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import com.seminarhub.global.repository.QuerydslRepositorySupport;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
@Repository
public class DeliveryRepositoryImpl extends QuerydslRepositorySupport<Delivery> implements DeliveryRepositoryCustom {
    public DeliveryRepositoryImpl(JPAQueryFactory f) { super(f, delivery, delivery.id, delivery.createdAt, delivery.updatedAt, delivery.deletedAt); }
    public List<Delivery> search(DeliverySearchQuery q) { return findAll(condition(q)); }
    public Page<Delivery> search(DeliverySearchQuery q, Pageable p) { return findAll(condition(q), p, this::sort); }
    public List<Delivery> search(DeliverySearchQuery q, CursorRequest c) { return findByCursor(condition(q), c); }
    private BooleanBuilder condition(DeliverySearchQuery q) {
        BooleanBuilder b = auditCondition(q);
        if (q.memberSeminarId() != null) b.and(delivery.memberSeminar.id.eq(q.memberSeminarId()));
        if (!CollectionUtils.isEmpty(q.memberSeminarIds())) b.and(delivery.memberSeminar.id.in(q.memberSeminarIds()));
        if (q.deliveryStatus() != null) b.and(delivery.deliveryStatus.eq(q.deliveryStatus()));
        if (!CollectionUtils.isEmpty(q.deliveryStatuses())) b.and(delivery.deliveryStatus.in(q.deliveryStatuses()));
        if (StringUtils.hasText(q.trackingNumber())) b.and(delivery.trackingNumber.containsIgnoreCase(q.trackingNumber()));
        if (StringUtils.hasText(q.courierCompany())) b.and(delivery.courierCompany.containsIgnoreCase(q.courierCompany()));
        return b;
    }
    private ComparableExpressionBase<?> sort(String n) { return switch (n) {
        case "id" -> delivery.id; case "memberSeminarId" -> delivery.memberSeminar.id;
        case "deliveryStatus" -> delivery.deliveryStatus; case "trackingNumber" -> delivery.trackingNumber;
        case "courierCompany" -> delivery.courierCompany; case "createdAt" -> delivery.createdAt;
        case "updatedAt" -> delivery.updatedAt; case "deletedAt" -> delivery.deletedAt; default -> null; }; }
}
