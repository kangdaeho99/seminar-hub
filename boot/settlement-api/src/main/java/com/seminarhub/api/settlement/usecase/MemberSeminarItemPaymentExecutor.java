package com.seminarhub.api.settlement.usecase;

import com.seminarhub.statemachine.result.ItemTransitionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** A separate bean ensures that every bulk item call crosses the transaction proxy. */
@Component
@RequiredArgsConstructor
public class MemberSeminarItemPaymentExecutor {

    private final MemberSeminarItemUseCase useCase;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ItemTransitionResult confirmPayment(Long orderId, Long itemId) {
        return useCase.confirmPayment(orderId, itemId);
    }
}
