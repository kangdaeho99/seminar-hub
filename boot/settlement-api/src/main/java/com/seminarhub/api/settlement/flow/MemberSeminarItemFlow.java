package com.seminarhub.api.settlement.flow;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.seminarhub.api.settlement.usecase.MemberSeminarItemUseCase;
import com.seminarhub.statemachine.result.ItemTransitionResult;

@Component
@RequiredArgsConstructor
public class MemberSeminarItemFlow {

    private final MemberSeminarItemUseCase useCase;

    public ItemTransitionResult confirmPayment(Long orderId, Long itemId) {
        return useCase.confirmPayment(orderId, itemId);
    }

    public List<ItemTransitionResult> cancelAll(Long orderId, List<Long> itemIds) {
        return useCase.cancelAll(orderId, itemIds);
    }

    public List<ItemTransitionResult> cancelPartial(Long orderId, List<Long> itemIds) {
        return useCase.cancelPartial(orderId, itemIds);
    }
}
