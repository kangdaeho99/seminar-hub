package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.core.entity.Seminar;

public interface SeminarService {
    Long register(SeminarDTO seminarDTO) throws DuplicateSeminarException;

    SeminarDTO get(String seminar_no);

    void modify(SeminarDTO seminarDTO);

    void remove(String seminar_name);


    default Seminar dtoToEntity(SeminarDTO seminarDTO){
        Seminar seminar = Seminar.builder()
                .seminar_no(seminarDTO.getSeminar_no())
                .seminar_name(seminarDTO.getSeminar_name())
                .seminar_explanation(seminarDTO.getSeminar_explanation())
                .build();
        return seminar;
    }

    default SeminarDTO entityToDTO(Seminar seminar){
        SeminarDTO seminarDTO = SeminarDTO.builder()
                .seminar_no(seminar.getSeminar_no())
                .seminar_name(seminar.getSeminar_name())
                .seminar_explanation(seminar.getSeminar_explanation())
                .build();
        return seminarDTO;
    }

}
