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
import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.domain.seminar.domain.Seminar;
import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.service.MemberSeminarItemService;
import com.seminarhub.domain.seminar.service.MemberSeminarService;
import com.seminarhub.domain.seminar.service.SeminarService;
import com.seminarhub.error.BadRequestException;
import com.seminarhub.error.NotFoundException;
import com.seminarhub.statemachine.MemberSeminarItemStateMachine;
import com.seminarhub.statemachine.context.FullCancelContext;
import com.seminarhub.statemachine.context.CreateContext;
import com.seminarhub.statemachine.context.ConfirmPaymentContext;
import com.seminarhub.statemachine.context.PartialCancelContext;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;
import com.seminarhub.statemachine.result.ItemTransitionResult;

@Component
@RequiredArgsConstructor
public class MemberSeminarItemUseCase {

    private final MemberSeminarService memberSeminarService;
    private final SeminarService seminarService;
    private final MemberSeminarItemService memberSeminarItemService;
    private final StateMachineBuilder<
            MemberSeminarItemStateMachine,
            MemberSeminarItemStatus,
            MemberSeminarItemEvent,
            TransitionExecutionContext> stateMachineBuilder;

    @Transactional
    public MemberSeminarItem create(Long orderId, Long seminarId) {
        if (orderId == null || seminarId == null) {
            throw new BadRequestException("주문 ID와 세미나 ID가 필요합니다.");
        }
        MemberSeminar order = memberSeminarService.findByIdForUpdate(orderId)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다. id=" + orderId));
        Seminar seminar = seminarService.findById(seminarId)
                .orElseThrow(() -> new NotFoundException("세미나를 찾을 수 없습니다. id=" + seminarId));
        MemberSeminarItem item = MemberSeminarItem.builder()
                .memberSeminar(order)
                .seminar(seminar)
                .build();

        TransitionExecutionContext context = new TransitionExecutionContext(new CreateContext(item));
        MemberSeminarItemStateMachine stateMachine = stateMachineBuilder.newStateMachine(
                item.getMemberSeminarItemStatus());

        stateMachine.fire(MemberSeminarItemEvent.CREATE, context);

        ItemTransitionResult result = new ItemTransitionResult(item.getId(), context.getRejection());
        if (!result.isSuccess()) {
            throw new BadRequestException(result.getMessage());
        }
        return item;
    }

    @Transactional
    public ItemTransitionResult confirmPayment(Long orderId, Long itemId) {
        List<MemberSeminarItem> requestedItems = findRequestedItems(
                orderId, Collections.singletonList(itemId));
        MemberSeminarItem item = requestedItems.getFirst();
        TransitionExecutionContext context = new TransitionExecutionContext(new ConfirmPaymentContext(item));
        MemberSeminarItemStateMachine stateMachine = stateMachineBuilder.newStateMachine(
                item.getMemberSeminarItemStatus());

        stateMachine.fire(MemberSeminarItemEvent.CONFIRM_PAYMENT, context);

        return new ItemTransitionResult(item.getId(), context.getRejection());
    }

    @Transactional
    public List<ItemTransitionResult> cancelAll(Long orderId, List<Long> itemIds) {
        List<MemberSeminarItem> requestedItems = findRequestedItems(orderId, itemIds);
        List<ItemTransitionResult> results = new ArrayList<>(requestedItems.size());

        for (MemberSeminarItem item : requestedItems) {
            TransitionExecutionContext context = new TransitionExecutionContext(
                    new FullCancelContext(item, requestedItems));
            MemberSeminarItemStateMachine stateMachine = stateMachineBuilder.newStateMachine(
                    item.getMemberSeminarItemStatus());

            stateMachine.fire(MemberSeminarItemEvent.FULL_CANCEL, context);

            results.add(new ItemTransitionResult(item.getId(), context.getRejection()));
        }

        return results;
    }

    @Transactional
    public List<ItemTransitionResult> cancelPartial(Long orderId, List<Long> itemIds) {
        List<MemberSeminarItem> requestedItems = findRequestedItems(orderId, itemIds);
        List<ItemTransitionResult> results = new ArrayList<>(requestedItems.size());

        for (MemberSeminarItem item : requestedItems) {
            TransitionExecutionContext context = new TransitionExecutionContext(new PartialCancelContext(item));
            MemberSeminarItemStateMachine stateMachine = stateMachineBuilder.newStateMachine(
                    item.getMemberSeminarItemStatus());

            stateMachine.fire(MemberSeminarItemEvent.PARTIAL_CANCEL, context);

            results.add(new ItemTransitionResult(item.getId(), context.getRejection()));
        }

        return results;
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
