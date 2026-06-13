//package com.seminarhub.elasticsearch.document;
//
//import com.seminarhub.elasticsearch.helper.Indices;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.*;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@Document(indexName = Indices.SEMINAR_INDEX)
//@Setting(settingPath = "elasticsearch/mappings/es-seminar-settings.json")
//@Mapping(mappingPath = "elasticsearch/mappings/es-seminar-mapping.json")
//@ToString
//@Builder
//public class SeminarDocument {
//
//    @Id
//    @Field(type = FieldType.Keyword)
//    private Long seminar_no;
//
//    @Field(type = FieldType.Text)
//    private String seminar_name;
//
//    @Field(type = FieldType.Text)
//    private String seminar_explanation;
//
//    @Field(type = FieldType.Keyword)
//    private Long seminar_price;
//
//    @Field(type = FieldType.Keyword)
//    private Long seminar_max_participants;
//
//    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
//    private LocalDateTime inst_dt;
//
//    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
//    private LocalDateTime updt_dt;
//
//}
