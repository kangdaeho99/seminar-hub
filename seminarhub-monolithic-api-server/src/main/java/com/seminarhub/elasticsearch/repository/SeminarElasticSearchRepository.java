package com.seminarhub.elasticsearch.repository;

import com.seminarhub.elasticsearch.document.SeminarDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeminarElasticSearchRepository extends ElasticsearchRepository<SeminarDocument, Long> {

}
