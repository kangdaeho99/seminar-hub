package com.seminarhub.service;

import com.seminarhub.dto.SeminarDTO;

import java.util.List;

public interface SeminarService {
    int createSeminar(SeminarDTO seminar);
    SeminarDTO getSeminarById(Long seminar_no);
    List<SeminarDTO> getSeminarsByCompanyNo(Long company_no);
    int updateSeminar(SeminarDTO seminar);
    int softDeleteSeminar(Long seminar_no);
}