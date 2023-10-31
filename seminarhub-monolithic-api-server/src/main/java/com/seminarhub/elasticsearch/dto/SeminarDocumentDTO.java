package com.seminarhub.elasticsearch.dto;

import com.seminarhub.elasticsearch.helper.Indices;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Document(indexName = Indices.SEMINAR_INDEX)
@Builder
public class SeminarDocumentDTO {

    private Long seminar_no;

    private String seminar_name;

    private String seminar_explanation;

    private Long seminar_max_participants;

    private LocalDateTime inst_dt;

    private LocalDateTime updt_dt;

}