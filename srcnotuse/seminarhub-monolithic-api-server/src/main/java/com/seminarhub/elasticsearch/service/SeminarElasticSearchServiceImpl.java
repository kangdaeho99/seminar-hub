//package com.seminarhub.elasticsearch.service;
//
//import com.seminarhub.elasticsearch.document.SeminarDocument;
//import com.seminarhub.elasticsearch.dto.PageRequestDTO;
//import com.seminarhub.elasticsearch.repository.SeminarElasticSearchRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.client.elc.NativeQuery;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.Criteria;
//import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
//import org.springframework.data.elasticsearch.core.query.Query;
//import org.springframework.stereotype.Service;
//
////@Service
//@Log4j2
//@RequiredArgsConstructor
//public class SeminarElasticSearchServiceImpl implements SeminarElasticSearchService{
//    private final SeminarElasticSearchRepository seminarElasticSearchRepository;
//
//    private final ElasticsearchOperations elasticsearchOperations;
//
//    @Override
//    public void save(SeminarDocument seminarDocument){
//        seminarElasticSearchRepository.save(seminarDocument);
//    }
//
//    @Override
//    public SeminarDocument get(Long seminar_no){
//        return seminarElasticSearchRepository.findById(seminar_no).orElse(null);
//    }
//
//    @Override
//    public SearchHits<SeminarDocument> searchByExplanation(String seminar_name, int page, int size) {
//        Query query = NativeQuery.builder()
////                .withAggregation("seminar_name", Aggregation.of(a -> a
////                        .terms(ta -> ta.field("seminar_name").size(10))))
//                .withQuery(q -> q
//                        .match(m -> m
//                                .field("seminar_name")
//                                .query(seminar_name)
//                        )
//                )
//                .withPageable(PageRequest.of(page, size))
//                .build();
//
//        SearchHits<SeminarDocument> searchHits = elasticsearchOperations.search(query, SeminarDocument.class);
//        return searchHits;
//    }
//
//    @Override
//    public SearchHits<SeminarDocument> searchByNativeQueryKeywordAndType(PageRequestDTO pageRequestDTO, Pageable pageable) {
//        Query query = NativeQuery.builder()
//                .withQuery(q -> q
//                        .bool(b -> b
//                                .filter(f -> {
//                                            if (pageRequestDTO.getType().contains("seminar_name") && pageRequestDTO.getType().contains("seminar_explanation")) {
//                                                // seminar_name과 seminar_explanation이 둘다 주어진경우
//                                                return f.bool(b1 -> b1
//                                                        .should(mq -> mq
//                                                                .match(mq1 -> mq1
//                                                                        .field("seminar_name")
//                                                                        .query(pageRequestDTO.getKeyword())))
//                                                        .should(mq -> mq
//                                                                .match(mq1 -> mq1
//                                                                        .field("seminar_explanation")
//                                                                        .query(pageRequestDTO.getKeyword()))));
//                                            } else if(pageRequestDTO.getType().contains("seminar_name") && !pageRequestDTO.getType().contains("seminar_explanation")){
//                                                // seminar_name만 주어진 경우
//                                                return f.bool(b1 -> b1
//                                                        .should(mq -> mq
//                                                                .match(mq1 -> mq1
//                                                                        .field("seminar_name")
//                                                                        .query(pageRequestDTO.getKeyword()))));
//                                            } else if(!pageRequestDTO.getType().contains("seminar_name") && pageRequestDTO.getType().contains("seminar_explanation")){
//                                                // seminar_explanation만 주어진 경우
//                                                return f.bool(b1 -> b1
//                                                        .should(mq -> mq
//                                                                .match(mq1 -> mq1
//                                                                        .field("seminar_explanation")
//                                                                        .query(pageRequestDTO.getKeyword()))));
//                                            }
//                                            return f.bool(b1 -> b1);
//                                        }
//                                )
//                        )
//                )
//                .withPageable(pageable)
//                .build();
//        SearchHits<SeminarDocument> searchHits = elasticsearchOperations.search(query, SeminarDocument.class);
//        return searchHits;
//    }
//
//
//    @Override
//    public SearchHits<SeminarDocument> searchByKeywordAndType(PageRequestDTO pageRequestDTO, Pageable pageable) {
//        CriteriaQuery query = createSearchCriteriaQuery(pageRequestDTO,pageable);
//
//        SearchHits<SeminarDocument> searchHits = elasticsearchOperations.search(query, SeminarDocument.class);
//        return searchHits;
//    }
//
//    private CriteriaQuery createSearchCriteriaQuery(PageRequestDTO pageRequestDTO, Pageable pageable) {
//        Criteria criteria = new Criteria();
//        Criteria seminar_name_criteria = new Criteria("seminar_name");
//        Criteria seminar_explanation_criteria = new Criteria("seminar_explanation");
//
//        if(pageRequestDTO == null)
//            return new CriteriaQuery(new Criteria());
//
//        String keyword = pageRequestDTO.getKeyword();
//        if(pageRequestDTO.getType().contains("seminar_name")){
//            seminar_name_criteria.is(keyword);
//        }
//
//        if(pageRequestDTO.getType().contains("seminar_explanation")){
//            seminar_explanation_criteria.is(keyword);
//        }
//
//        CriteriaQuery query = new CriteriaQuery(seminar_name_criteria.or(seminar_explanation_criteria));
//        query.setPageable(pageable);
//        return query;
//    }
//
//
//
//}
