package com.seminarhub.statemachine.guard;

import java.util.List;

import com.seminarhub.domain.seminar.domain.MemberSeminarItem;
import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.domain.seminar.service.MemberSeminarItemService;
import com.seminarhub.statemachine.context.FullCancelContext;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.result.TransitionRejectionCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

@Component
@RequiredArgsConstructor
public class FullCancelGuard extends AnonymousCondition<TransitionExecutionContext> {

    private final MemberSeminarItemService memberSeminarItemService;

    @Override
    public boolean isSatisfied(TransitionExecutionContext context) {
        FullCancelContext fullCancelContext = (FullCancelContext) context.getRequest();

        List<MemberSeminarItem> orderItems =
                memberSeminarItemService.findByMemberSeminarId(
                        fullCancelContext.item().getMemberSeminar().getId()
                );

        for (MemberSeminarItem orderItem : orderItems) {
            if (orderItem.getMemberSeminarItemStatus()
                    == MemberSeminarItemStatus.CANCELLED) {
                continue;
            }

            if (orderItem.getMemberSeminarItemStatus()
                    != MemberSeminarItemStatus.PAID) {
                return context.reject(TransitionRejectionCode.CANCEL_ORDER_STATE_MISMATCH);
            }

            boolean requested = false;

            for (MemberSeminarItem requestedItem : fullCancelContext.requestedItems()) {
                if (orderItem.getId().equals(requestedItem.getId())) {
                    requested = true;
                    break;
                }
            }

            if (!requested) {
                return context.reject(TransitionRejectionCode.CANCEL_ITEMS_INCOMPLETE);
            }
        }

        return true;
    }
}
