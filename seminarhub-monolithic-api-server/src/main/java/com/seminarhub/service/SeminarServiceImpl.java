package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.dto.SeminarPageResultDTO;
import com.seminarhub.entity.Seminar;
import com.seminarhub.repository.SeminarQuerydslRepository;
import com.seminarhub.repository.SeminarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description: Implementation of SeminarService interface for managing seminar-related operations
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class SeminarServiceImpl implements  SeminarService{

    private final SeminarRepository seminarRepository;

    private final SeminarQuerydslRepository seminarQuerydslRepository;

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Register a new seminar.
     * If a seminar with the same name already exists, a DuplicateSeminarException is thrown.
     */
    @Override
    public Long register(SeminarDTO seminarDTO) throws DuplicateSeminarException {
        Seminar seminar = dtoToEntity(seminarDTO);
        log.info("=================Seminar Register======================");
        log.info(seminar);

        if(seminarRepository.findBySeminar_name(seminarDTO.getSeminar_name()).isPresent()){
            throw new DuplicateSeminarException(seminarDTO.getSeminar_name() + "is Already Exists");
        }

        seminarRepository.save(seminar);

        return seminar.getSeminar_no();
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Retrieve a seminar by its seminar_name.
     * If the seminar exists, its data is converted to DTO and returned.
     */
    @Override
    public SeminarDTO get(String seminar_name) {
        Optional<Seminar> result = seminarRepository.findBySeminar_name(seminar_name);
        if(result.isPresent()){
            return entityToDTO(result.get());
        }
        return null;
    }



    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Modify an existing seminar's information.
     * The seminar_name is used to identify the seminar to be modified.
     */
    @Override
    public void modify(SeminarDTO seminarDTO) {
        String seminar_name = seminarDTO.getSeminar_name();
        System.out.println("seminar_name:"+seminar_name);
        Optional<Seminar> result = seminarRepository.findBySeminar_name(seminar_name);
        if(result.isPresent()){
            Seminar seminar = result.get();
            seminar.setSeminar_name(seminarDTO.getSeminar_name());
            seminarRepository.save(seminar);
        }
    }

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description: Soft remove a seminar by its seminar_name.
     * Using Jpa Dirty Checking
     */
    @Override
    public void remove(String seminar_name) {
        Optional<Seminar> result = seminarRepository.findBySeminar_name(seminar_name);
        if(result.isPresent()){
            System.out.println("result"+result);
            result.get().setDel_dt(LocalDateTime.now());
//            seminarRepository.deleteBySeminar_name(seminar_name);
        }
    }

    @Override
    public List<SeminarPageResultDTO> list(int pageNo, int pageSize) {
        List<SeminarPageResultDTO> result = seminarQuerydslRepository.mainPagePagingSeminarWithEhCache(pageNo, pageSize);
        return result;
    }

    @Override
    public List<SeminarPageResultDTO> mainPagelistWithCoveringIndexAndEhCache(int pageNo, int pageSize) {
        List<SeminarPageResultDTO> result = seminarQuerydslRepository.mainPagePagingSeminarWithCoveringIndexAndEhCache(pageNo, pageSize);
        return result;
    }


}
