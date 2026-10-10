package com.seminarhub.api.settlement.usecase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;

import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.service.MemberSeminarItemService;
import com.seminarhub.domain.seminar.service.MemberSeminarService;
import com.seminarhub.error.BadRequestException;
import com.seminarhub.error.NotFoundException;
import com.seminarhub.statemachine.MemberSeminarItemStateMachine;
import com.seminarhub.statemachine.context.FullCancelContext;
import com.seminarhub.statemachine.context.ConfirmPaymentContext;
import com.seminarhub.statemachine.context.MemberSeminarItemContext;
import com.seminarhub.statemachine.context.PartialCancelContext;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;
import com.seminarhub.statemachine.result.ItemTransitionResult;

@Component
@RequiredArgsConstructor
public class MemberSeminarItemUseCase {

    private final MemberSeminarService memberSeminarService;
    private final MemberSeminarItemService memberSeminarItemService;
    private final StateMachineBuilder<
            MemberSeminarItemStateMachine,
            MemberSeminarItemStatus,
            MemberSeminarItemEvent,
            TransitionExecutionContext> stateMachineBuilder;

    @Transactional
    public ItemTransitionResult confirmPayment(Long orderId, Long itemId) {
        List<MemberSeminarItem> requestedItems = findRequestedItems(
                orderId, Collections.singletonList(itemId));
        return execute(MemberSeminarItemEvent.CONFIRM_PAYMENT,
                new ConfirmPaymentContext(requestedItems.getFirst()));
    }

    @Transactional
    public List<ItemTransitionResult> cancelAll(Long orderId, List<Long> itemIds) {
        List<MemberSeminarItem> requestedItems = findRequestedItems(orderId, itemIds);
        List<ItemTransitionResult> results = new ArrayList<>(requestedItems.size());

        for (MemberSeminarItem item : requestedItems) {
            results.add(execute(MemberSeminarItemEvent.FULL_CANCEL,
                    new FullCancelContext(item, requestedItems)));
        }

        return results;
    }

    @Transactional
    public List<ItemTransitionResult> cancelPartial(Long orderId, List<Long> itemIds) {
        List<MemberSeminarItem> requestedItems = findRequestedItems(orderId, itemIds);
        List<ItemTransitionResult> results = new ArrayList<>(requestedItems.size());

        for (MemberSeminarItem item : requestedItems) {
            results.add(execute(MemberSeminarItemEvent.PARTIAL_CANCEL,
                    new PartialCancelContext(item)));
        }

        return results;
    }

    private ItemTransitionResult execute(
            MemberSeminarItemEvent event,
            MemberSeminarItemContext request
    ) {
        TransitionExecutionContext context = new TransitionExecutionContext(request);
        MemberSeminarItemStateMachine stateMachine = stateMachineBuilder.newStateMachine(
                request.item().getMemberSeminarItemStatus());

        stateMachine.fire(event, context);

        return new ItemTransitionResult(request.item().getId(), context.getRejection());
    }

    private List<MemberSeminarItem> findRequestedItems(Long orderId, List<Long> itemIds) {
        if (orderId == null || itemIds == null || itemIds.isEmpty()
                || itemIds.stream().anyMatch(Objects::isNull)) {
            throw new BadRequestException("주문 ID와 처리할 항목 ID가 필요합니다.");
        }

        memberSeminarService.findByIdForUpdate(orderId)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다. id=" + orderId));

        List<MemberSeminarItem> orderItems = memberSeminarItemService.findByMemberSeminarId(orderId);
        Map<Long, MemberSeminarItem> itemsById = new HashMap<>();
        for (MemberSeminarItem item : orderItems) {
            itemsById.put(item.getId(), item);
        }

        List<MemberSeminarItem> requestedItems = new ArrayList<>();
        for (Long itemId : new LinkedHashSet<>(itemIds)) {
            MemberSeminarItem item = itemsById.get(itemId);
            if (item == null) {
                throw new BadRequestException("주문에 해당 항목이 없습니다. itemId=" + itemId);
            }
            requestedItems.add(item);
        }
        return requestedItems;
    }
}
