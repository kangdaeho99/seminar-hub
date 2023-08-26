package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.entity.Member;
import com.seminarhub.entity.Member_Seminar;
import com.seminarhub.dto.MemberSeminarDTO;
import com.seminarhub.entity.Seminar;

import java.util.List;

public interface MemberSeminarService {
    Long register(MemberSeminarDTO memberSeminarDTO);

    MemberSeminarDTO get(Long member_seminar_no);

    void remove(Long member_seminar_no);

    List<MemberSeminarDTO> getListByMember_id(String member_id);

    default Member_Seminar dtoToEntity(MemberSeminarDTO memberSeminarDTO){

        Member member = Member.builder()
                .member_id(memberSeminarDTO.getMember_id())
                .build();

        Seminar seminar = Seminar.builder()
                .seminar_name(memberSeminarDTO.getSeminar_name())
                .build();

        Member_Seminar member_seminar = Member_Seminar.builder()
                .member_seminar_no(memberSeminarDTO.getMember_seminar_no())
                .member(member)
                .seminar(seminar)
                .build();

        return member_seminar;
    }

    default MemberSeminarDTO entityToDTO(Member_Seminar member_seminar){
        MemberSeminarDTO memberSeminarDTO = MemberSeminarDTO.builder()
                .member_seminar_no(member_seminar.getMember_seminar_no())
                .member_id(member_seminar.getMember().getMember_id())
                .seminar_name(member_seminar.getSeminar().getSeminar_name())
                .build();
        return memberSeminarDTO;
    }

}
