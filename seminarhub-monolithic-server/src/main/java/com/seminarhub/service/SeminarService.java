package com.seminarhub.service;

import com.seminarhub.entity.SeminarDTO;

import java.util.List;

public interface SeminarService {
    int decrementSeminarAvailableSeats(SeminarDTO seminar);
    int createSeminar(SeminarDTO seminar);
    SeminarDTO getSeminarBySeminarNoWithPessimisticLock(Long seminar_no);
    SeminarDTO getSeminarById(Long seminar_no);
    List<SeminarDTO> getSeminarsByCompanyNo(Long company_no);
    int updateSeminar(SeminarDTO seminar);
    int softDeleteSeminar(Long seminar_no);

}