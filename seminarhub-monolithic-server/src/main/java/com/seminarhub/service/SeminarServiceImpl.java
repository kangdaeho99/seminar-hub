package com.seminarhub.service;

import com.seminarhub.entity.SeminarDTO;
import com.seminarhub.repository.SeminarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeminarServiceImpl implements SeminarService {
    private final SeminarRepository seminarRepository;

    public SeminarServiceImpl(SeminarRepository seminarRepository) {
        this.seminarRepository = seminarRepository;
    }


    @Override
    public int decrementSeminarAvailableSeats(SeminarDTO seminar) {
        return seminarRepository.decrementAvailableSeats(seminar);
    }

    @Override
    public int createSeminar(SeminarDTO seminar) {
        return seminarRepository.insert(seminar);
    }

    @Override
    public SeminarDTO getSeminarBySeminarNoWithPessimisticLock(Long seminar_no) {
        return seminarRepository.findSeminarBySeminarNoWithPessimisticLock(seminar_no);
    }

    @Override
    public SeminarDTO getSeminarById(Long seminar_no) {
        return seminarRepository.findSeminarById(seminar_no.intValue());
    }

    @Override
    public List<SeminarDTO> getSeminarsByCompanyNo(Long company_no) {
        return seminarRepository.findSeminarsByCompanyNo(company_no.intValue());
    }

    @Override
    public int updateSeminar(SeminarDTO seminar) {
        return seminarRepository.update(seminar);
    }

    @Override
    public int softDeleteSeminar(Long seminar_no) {
        return seminarRepository.softDelete(seminar_no);
    }

}