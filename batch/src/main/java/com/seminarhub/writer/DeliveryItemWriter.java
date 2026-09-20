package com.seminarhub.writer;

import com.seminarhub.entity.Delivery;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;

public class DeliveryItemWriter extends JpaItemWriter<Delivery> {

    public DeliveryItemWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}
