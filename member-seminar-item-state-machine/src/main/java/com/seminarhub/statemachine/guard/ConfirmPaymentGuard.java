package com.seminarhub.statemachine.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.fsm.AnonymousCondition;

import com.seminarhub.domain.payment.service.PaymentService;
import com.seminarhub.statemachine.context.TransitionExecutionContext;
import com.seminarhub.statemachine.result.TransitionRejectionCode;

@Component
@RequiredArgsConstructor
public class ConfirmPaymentGuard extends AnonymousCondition<TransitionExecutionContext> {

    private final PaymentService paymentService;

    @Override
    public boolean isSatisfied(TransitionExecutionContext context) {
        if (!paymentService.existsSuccessfulPayment(context.getRequest().item().getId())) {
            return context.reject(TransitionRejectionCode.PAYMENT_NOT_CONFIRMED);
        }
        return true;
    }
}
