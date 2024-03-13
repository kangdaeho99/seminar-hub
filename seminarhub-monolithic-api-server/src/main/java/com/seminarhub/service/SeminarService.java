package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.entity.Seminar;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description: Service interface for managing Seminar-related operations
 */
public interface SeminarService {

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Register a new seminar.
     * @throws DuplicateSeminarException if the seminar with the same name already exists.
     */
    Long register(SeminarDTO seminarDTO) throws DuplicateSeminarException;

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Retrieve a seminar by its seminar_no.
     */
    SeminarDTO get(String seminar_name);

    SeminarDTO getWithPessimisticLock(String seminar_name);

    SeminarDTO getBySeminar_NoWithPessimisticLock(Long seminar_no);

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Modify an existing seminar's information.
     */
    void modify(SeminarDTO seminarDTO);

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Soft remove a seminar by its seminar_name.
     */
    void remove(String seminar_name);

    /**
     * [ 2023-12-18 daeho.kang ]
     * Description : AOP Test
     *
     */
    String printOutString(String str);

    SeminarDTO getBySeminar_name(String seminar_name);

    void increaseParticipantsCnt(long seminar_no);

    void decreaseParticipantsCnt(long seminar_no);
    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Convert a SeminarDTO object to a Seminar entity.
     */
    default Seminar dtoToEntity(SeminarDTO seminarDTO){
        Seminar seminar = Seminar.builder()
                .seminar_no(seminarDTO.getSeminar_no())
                .seminar_name(seminarDTO.getSeminar_name())
                .seminar_explanation(seminarDTO.getSeminar_explanation())
                .seminar_price(seminarDTO.getSeminar_price())
                .seminar_max_participants(seminarDTO.getSeminar_max_participants())
                .seminar_participants_cnt(seminarDTO.getSeminar_participants_cnt())
                .build();
        return seminar;
    }

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Convert a Seminar entity to a SeminarDTO object.
     */
    default SeminarDTO entityToDTO(Seminar seminar){
        SeminarDTO seminarDTO = SeminarDTO.builder()
                .seminar_no(seminar.getSeminar_no())
                .seminar_name(seminar.getSeminar_name())
                .seminar_explanation(seminar.getSeminar_explanation())
                .seminar_max_participants(seminar.getSeminar_max_participants())
                .seminar_participants_cnt(seminar.getSeminar_participants_cnt())
                .seminar_price(seminar.getSeminar_price())
                .build();
        return seminarDTO;
    }

}
