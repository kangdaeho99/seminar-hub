package com.seminarhub.domain.delivery.repository.querydsl;
import com.seminarhub.domain.delivery.domain.Delivery;
import com.seminarhub.domain.delivery.service.DeliverySearchQuery;
import com.seminarhub.global.dto.CursorRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface DeliveryRepositoryCustom {
    List<Delivery> search(DeliverySearchQuery query);
    Page<Delivery> search(DeliverySearchQuery query, Pageable pageable);
    List<Delivery> search(DeliverySearchQuery query, CursorRequest cursorRequest);
}
