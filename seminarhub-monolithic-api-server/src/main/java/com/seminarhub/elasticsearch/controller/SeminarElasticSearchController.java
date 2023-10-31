package com.seminarhub.elasticsearch.controller;

import com.seminarhub.elasticsearch.document.SeminarDocument;
import com.seminarhub.elasticsearch.service.SeminarElasticSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/v1/seminar/es")
@RequiredArgsConstructor
public class SeminarElasticSearchController {

    private final SeminarElasticSearchService seminarElasticSearchService;

    @PostMapping(value = "/")
    public void save(@RequestBody SeminarDocument seminarDocument){
        seminarElasticSearchService.save(seminarDocument);
    }

    @GetMapping("/{seminar_no}")
    public ResponseEntity<SeminarDocument> get(@PathVariable("seminar_no") Long seminar_no){
        return new ResponseEntity<>( seminarElasticSearchService.get(seminar_no), HttpStatus.OK);
    }

    @GetMapping("/list/{seminar_name}")
    public ResponseEntity<SearchHits<SeminarDocument>> get(@PathVariable("seminar_name") String seminar_name){
        return new ResponseEntity<>( seminarElasticSearchService.searchByExplanation(seminar_name, 0, 10), HttpStatus.OK);
    }

}
