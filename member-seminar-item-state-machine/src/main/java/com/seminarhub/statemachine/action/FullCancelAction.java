package com.seminarhub.statemachine.action;

import java.util.List;

import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.AnonymousAction;

import com.seminarhub.domain.seminar.domain.MemberSeminar;
import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.enums.MemberSeminarStatus;
import com.seminarhub.domain.seminar.service.MemberSeminarItemService;
import com.seminarhub.statemachine.MemberSeminarItemStateMachine;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FullCancelAction
        extends AnonymousAction<
                MemberSeminarItemStateMachine,
                MemberSeminarItemStatus,
                MemberSeminarItemEvent,
                TransitionExecutionContext> {

    private final MemberSeminarItemService memberSeminarItemService;

    @Override
    public void execute(
            MemberSeminarItemStatus from,
            MemberSeminarItemStatus to,
            MemberSeminarItemEvent event,
            TransitionExecutionContext context,
            MemberSeminarItemStateMachine stateMachine
    ) {
        MemberSeminarItem item = context.getRequest().item();
        MemberSeminar order = item.getMemberSeminar();

        List<MemberSeminarItem> orderItems =
                memberSeminarItemService.findByMemberSeminarId(
                        order.getId()
                );

        item.changeStatus(to);

        for (MemberSeminarItem orderItem : orderItems) {
            if (orderItem.getMemberSeminarItemStatus()
                    != MemberSeminarItemStatus.CANCELLED) {
                order.changeStatus(MemberSeminarStatus.PARTIAL_CANCELLED);
                return;
            }
        }

        order.changeStatus(MemberSeminarStatus.FULL_CANCELED);
    }
}
