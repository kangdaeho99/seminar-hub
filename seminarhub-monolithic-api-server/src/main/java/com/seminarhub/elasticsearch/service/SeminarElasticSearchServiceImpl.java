package com.seminarhub.elasticsearch.service;

import com.seminarhub.elasticsearch.document.SeminarDocument;
import com.seminarhub.elasticsearch.repository.SeminarElasticSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class SeminarElasticSearchServiceImpl implements SeminarElasticSearchService{
    private final SeminarElasticSearchRepository seminarElasticSearchRepository;

    public void save(SeminarDocument seminarDocument){
        seminarElasticSearchRepository.save(seminarDocument);
    }

    public SeminarDocument get(Long seminar_no){
        return seminarElasticSearchRepository.findById(seminar_no).orElse(null);
    }


}
