package com.seminarhub.api.settlement.flow;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.exception.SquirrelRuntimeException;

import com.seminarhub.api.settlement.usecase.MemberSeminarItemPaymentExecutor;
import com.seminarhub.api.settlement.usecase.MemberSeminarItemUseCase;
import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.error.BadRequestException;
import com.seminarhub.statemachine.exception.PaymentNotConfirmedException;
import com.seminarhub.statemachine.result.ItemTransitionResult;

@Component
@RequiredArgsConstructor
public class MemberSeminarItemFlow {

    private final MemberSeminarItemUseCase useCase;
    private final MemberSeminarItemPaymentExecutor paymentExecutor;

    public MemberSeminarItem create(Long orderId, Long seminarId) {
        return useCase.create(orderId, seminarId);
    }

    public ItemTransitionResult confirmPayment(Long orderId, Long itemId) {
        return useCase.confirmPayment(orderId, itemId);
    }

    /**
     * Commits each item independently and reports expected payment rejections in request order.
     * Unexpected failures stop processing; previously committed items remain committed.
     */
    public List<ItemTransitionResult> confirmPaymentsPartial(Long orderId, List<Long> itemIds) {
        if (orderId == null || orderId <= 0 || itemIds == null || itemIds.isEmpty()
                || itemIds.stream().anyMatch(itemId -> itemId == null || itemId <= 0)) {
            throw new BadRequestException("주문 ID와 처리할 양수 항목 ID가 필요합니다.");
        }

        List<ItemTransitionResult> results = new ArrayList<>();
        for (Long itemId : new LinkedHashSet<>(itemIds)) {
            try {
                // Commit/rollback completes in the executor proxy before control returns here.
                results.add(paymentExecutor.confirmPayment(orderId, itemId));
            } catch (PaymentNotConfirmedException | SquirrelRuntimeException exception) {
                Throwable cause = exception;
                while (cause instanceof SquirrelRuntimeException squirrel
                        && squirrel.getTargetException() != null
                        && squirrel.getTargetException() != cause) {
                    // Squirrel 0.3.10 keeps its target exception outside Throwable.getCause().
                    cause = squirrel.getTargetException();
                }
                if (!(cause instanceof PaymentNotConfirmedException rejection)
                        || !Objects.equals(itemId, rejection.getItemId())) {
                    throw exception;
                }
                results.add(new ItemTransitionResult(itemId, rejection.getCode()));
            }
        }
        return List.copyOf(results);
    }

    public List<ItemTransitionResult> cancelAll(Long orderId, List<Long> itemIds) {
        return useCase.cancelAll(orderId, itemIds);
    }

    public List<ItemTransitionResult> cancelPartial(Long orderId, List<Long> itemIds) {
        return useCase.cancelPartial(orderId, itemIds);
    }
}
