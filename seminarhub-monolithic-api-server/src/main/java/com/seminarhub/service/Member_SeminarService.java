package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.common.exception.SeminarRegistrationFullException;
import com.seminarhub.dto.MemberSeminarRegisterRequestDTO;
import com.seminarhub.dto.Member_SeminarDTO;
import com.seminarhub.entity.Member;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.entity.Member_Seminar_Payment_History;
import com.seminarhub.entity.Seminar;

public interface Member_SeminarService {
    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Register a new seminar.
     * @throws DuplicateSeminarException if the seminar with the same name already exists.
     */
    Long register(Member_SeminarDTO Member_SeminarDTO);

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Retrieve a seminar by its seminar_no.
     */
    Member_SeminarDTO get(long member_seminarno);

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Modify an existing seminar's information.
     */
    void modify(Member_SeminarDTO Member_SeminarDTO);

    Long registerForSeminar(MemberSeminarRegisterRequestDTO memberSeminarRegisterRequestDTO) throws SeminarRegistrationFullException;

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Soft remove a seminar by its seminar_name.
     */
    void remove(long member_seminarno);

    default Member_Seminar dtoToEntity(Member_SeminarDTO member_seminarDTO){
        Member member = Member.builder()
                .member_no(member_seminarDTO.getMember_no())
                .build();

        Seminar seminar = Seminar.builder()
                .seminar_no(member_seminarDTO.getSeminar_no())
                .build();

        Member_Seminar_Payment_History member_seminar_payment_history = Member_Seminar_Payment_History.builder()
                .member_seminar_payment_history_no(member_seminarDTO.getMember_seminar_payment_history_no())
                .build();

        Member_Seminar member_seminar = Member_Seminar.builder()
                .member(member)
                .seminar(seminar)
                .member_seminar_payment_history(member_seminar_payment_history)
                .build();
        return member_seminar;
    }

    default Member_SeminarDTO entityToDTO(Member_Seminar member_seminar){
        Member_SeminarDTO member_seminarDTO = Member_SeminarDTO.builder()
                .member_seminar_no(member_seminar.getMember_seminar_no())
                .seminar_no(member_seminar.getSeminar().getSeminar_no())
                .member_no(member_seminar.getMember().getMember_no())
                .build();
        return member_seminarDTO;
    }

}
