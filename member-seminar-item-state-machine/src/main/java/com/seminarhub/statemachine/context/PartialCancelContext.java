package com.seminarhub.statemachine.context;

import com.seminarhub.domain.seminar.domain.MemberSeminarItem;

public record PartialCancelContext(
    MemberSeminarItem item
)  implements MemberSeminarItemContext{
}
