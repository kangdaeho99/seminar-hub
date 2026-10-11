package com.seminarhub.statemachine.action;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.AnonymousAction;

import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.domain.seminar.service.MemberSeminarItemStatusHistoryService;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;
import com.seminarhub.statemachine.MemberSeminarItemStateMachine;
import com.seminarhub.statemachine.context.TransitionExecutionContext;

@Component
@RequiredArgsConstructor
public class ConfirmPaymentAction
        extends AnonymousAction<MemberSeminarItemStateMachine, MemberSeminarItemStatus,
                MemberSeminarItemEvent, TransitionExecutionContext> {

    private final MemberSeminarItemStatusHistoryService historyService;

    @Override
    public void execute(
            MemberSeminarItemStatus from,
            MemberSeminarItemStatus to,
            MemberSeminarItemEvent event,
            TransitionExecutionContext context,
            MemberSeminarItemStateMachine stateMachine
    ) {
        MemberSeminarItem item = context.getRequest().item();
        item.changeStatus(to);
        historyService.record(item, to);
    }
}
