package com.seminarhub.statemachine.context;

import com.seminarhub.domain.seminar.domain.MemberSeminarItem;

public record ConfirmPaymentContext(MemberSeminarItem item) implements MemberSeminarItemContext{
    
}
