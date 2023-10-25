package com.seminarhub.elasticsearch.service;

import com.seminarhub.elasticsearch.document.SeminarDocument;

public interface SeminarElasticSearchService {

    void save(SeminarDocument seminarDocument);

    SeminarDocument get(Long seminar_no);


}
