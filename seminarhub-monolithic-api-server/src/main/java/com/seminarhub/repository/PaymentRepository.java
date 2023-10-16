package com.seminarhub.repository;

import com.seminarhub.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description: Repository interface for managing payment entities
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Retrieve a payment by its payment_name while ensuring it's not deleted (del_dt is null)
     */
    @Query("SELECT p FROM Payment p WHERE p.payment_no = :payment_no AND p.del_dt is null")
    Optional<Payment> findBypayment_no(@Param("payment_no") Long payment_no);


}
