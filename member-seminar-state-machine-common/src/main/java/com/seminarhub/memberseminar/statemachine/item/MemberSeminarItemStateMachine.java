package com.seminarhub.memberseminar.statemachine.item;

import com.seminarhub.domain.seminar.enums.MemberSeminarItemStatus;
import com.seminarhub.memberseminar.statemachine.MemberSeminarEvent;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

public class MemberSeminarItemStateMachine extends AbstractStateMachine<
        MemberSeminarItemStateMachine,
        MemberSeminarItemStatus,
        MemberSeminarEvent,
        MemberSeminarItemStateMachineContext
        > {
}
