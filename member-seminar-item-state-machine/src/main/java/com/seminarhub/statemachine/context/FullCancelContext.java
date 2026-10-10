package com.seminarhub.statemachine.context;

import java.util.List;

import com.seminarhub.domain.seminar.domain.MemberSeminarItem;

public record FullCancelContext(
    MemberSeminarItem item,
    List<MemberSeminarItem> requestedItems
) implements MemberSeminarItemContext {
    
}
