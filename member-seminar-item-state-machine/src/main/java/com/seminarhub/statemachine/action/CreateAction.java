package com.seminarhub.statemachine.action;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.AnonymousAction;

import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.service.MemberSeminarItemService;
import com.seminarhub.domain.seminar.service.MemberSeminarItemStatusHistoryService;
import com.seminarhub.statemachine.MemberSeminarItemStateMachine;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.event.MemberSeminarItemEvent;

@Component
@RequiredArgsConstructor
public class CreateAction extends AnonymousAction<MemberSeminarItemStateMachine,
        MemberSeminarItemStatus, MemberSeminarItemEvent, TransitionExecutionContext> {

    private final MemberSeminarItemService memberSeminarItemService;
    private final MemberSeminarItemStatusHistoryService historyService;

    @Override
    public void execute(
            MemberSeminarItemStatus from,
            MemberSeminarItemStatus to,
            MemberSeminarItemEvent event,
            TransitionExecutionContext context,
            MemberSeminarItemStateMachine stateMachine
    ) {
        memberSeminarItemService.save(context.getRequest().item());
        historyService.record(context.getRequest().item(), to);
    }
}
