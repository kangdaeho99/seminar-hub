package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.dto.SeminarPageResultDTO;
import com.seminarhub.entity.Seminar;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
    SeminarDTO get(String seminar_no);

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

    List<SeminarPageResultDTO> list(int pageNo, int pageSize);

    List<SeminarPageResultDTO> mainPagelistWithCoveringIndexAndEhCache(int pageNo, int pageSize);

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description: Convert a SeminarDTO object to a Seminar entity.
     */
    default Seminar dtoToEntity(SeminarDTO seminarDTO){
        Seminar seminar = Seminar.builder()
                .seminar_no(seminarDTO.getSeminar_no())
                .seminar_name(seminarDTO.getSeminar_name())
                .seminar_explanation(seminarDTO.getSeminar_explanation())
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
                .build();
        return seminarDTO;
    }

}
