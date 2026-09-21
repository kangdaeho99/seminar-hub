package com.seminarhub.batch.delivery.writer;

import com.seminarhub.domain.delivery.domain.Delivery;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;

public class DeliveryItemWriter extends JpaItemWriter<Delivery> {

    public DeliveryItemWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}
