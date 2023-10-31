package com.seminarhub.elasticsearch.service;

import com.seminarhub.elasticsearch.document.SeminarDocument;
import com.seminarhub.elasticsearch.dto.PageRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface SeminarElasticSearchService {

    void save(SeminarDocument seminarDocument);

    SeminarDocument get(Long seminar_no);

    SearchHits<SeminarDocument> searchByExplanation(String explanation, int page, int size);

    SearchHits<SeminarDocument> searchByKeywordAndType(PageRequestDTO pageRequestDTO, Pageable pageable);

    SearchHits<SeminarDocument> searchByNativeQueryKeywordAndType(PageRequestDTO pageRequestDTO, Pageable pageable);

}
