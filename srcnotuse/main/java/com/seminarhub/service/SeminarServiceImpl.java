package com.seminarhub.service;

import com.seminarhub.common.exception.DuplicateSeminarException;
import com.seminarhub.dto.SeminarDTO;
import com.seminarhub.entity.Seminar;
import com.seminarhub.repository.SeminarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class SeminarServiceImpl implements  SeminarService{

    private final SeminarRepository seminarRepository;

    /**
     * [ 2023-08-10 daeho.kang ]
     * Description :
     * seminarName을 통해 이미 중복하는 아이디가 존재하는지 확인합니다. 중복일시 DuplicateSeminarException 이 발생합니다.
     * 통과하였다면, 세미나가 저장됩니다.
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
     * Description :
     * seminar_name을 통해 seminar의 정보를 가져옵니다.
     * 정보가 존재한다면, 해당 데이터를 DTO로 변환하여 전달합니다.
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
     * Description :
     * seminar_name을 통해 해당 값이 존재하는지 확인하고, 존재한다면 수정합니다. (seminar_no가 아닌 seminar_name을 통해 진행합니다.)
     * seminar_name 이 Unique 값입니다.
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
     * Description :
     * seminar_name을 통해 해당 값이 존재하는지 확인하고, 값이 존재한다면 SOFT DELETE 처리합니다.
     */
    @Override
    public void remove(String seminar_name) {
        Optional<Seminar> result = seminarRepository.findBySeminar_name(seminar_name);
        if(result.isPresent()){
            System.out.println("result"+result);
            seminarRepository.deleteBySeminar_name(seminar_name);
        }
    }


}
