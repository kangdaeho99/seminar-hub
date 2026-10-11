package com.seminarhub.statemachine.context;

import com.seminarhub.domain.seminar.domain.MemberSeminarItem;

public record CreateContext(MemberSeminarItem item) implements MemberSeminarItemContext {
}
