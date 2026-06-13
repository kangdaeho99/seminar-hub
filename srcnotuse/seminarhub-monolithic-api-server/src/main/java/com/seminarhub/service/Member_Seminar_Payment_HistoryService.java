package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.Member_Seminar_Payment_HistoryDTO;
import com.seminarhub.entity.Member_Seminar_Payment_History;

public interface Member_Seminar_Payment_HistoryService {

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Register a new seminar.
     * @throws DuplicateSeminarException if the seminar with the same name already exists.
     */
    Long register(Member_Seminar_Payment_HistoryDTO member_seminar_payment_historyDTO);

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Retrieve a seminar by its seminar_no.
     */
    Member_Seminar_Payment_HistoryDTO get(long member_seminar_payment_history_no);

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Modify an existing seminar's information.
     */
    void modify(Member_Seminar_Payment_HistoryDTO member_seminar_payment_historyDTO);

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Soft remove a seminar by its seminar_name.
     */
    void remove(long member_seminar_payment_history_no);

    default Member_Seminar_Payment_History dtoToEntity(Member_Seminar_Payment_HistoryDTO member_seminar_payment_historyDTO){
        Member_Seminar_Payment_History member_seminar_payment_history = Member_Seminar_Payment_History.builder()
                .member_seminar_payment_history_no(member_seminar_payment_historyDTO.getMember_seminar_payment_history_no())
                .member_seminar_payment_history_amount(member_seminar_payment_historyDTO.getMember_seminar_payment_history_amount())
                .build();
        return member_seminar_payment_history;
    }

    default Member_Seminar_Payment_HistoryDTO entityToDTO(Member_Seminar_Payment_History member_seminar_payment_history){
        Member_Seminar_Payment_HistoryDTO member_seminar_payment_historyDTO = Member_Seminar_Payment_HistoryDTO.builder()
                .member_seminar_payment_history_no(member_seminar_payment_history.getMember_seminar_payment_history_no())
                .member_seminar_payment_history_amount(member_seminar_payment_history.getMember_seminar_payment_history_amount())
                .build();
        return member_seminar_payment_historyDTO;
    }
}
