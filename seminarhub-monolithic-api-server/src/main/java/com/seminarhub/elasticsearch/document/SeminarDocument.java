package com.seminarhub.elasticsearch.document;

import com.seminarhub.elasticsearch.helper.Indices;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@Document(indexName = Indices.SEMINAR_INDEX)
@Setting(settingPath = "es-settings.json")
public class SeminarDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private Long seminar_no;

    @Field(type = FieldType.Text)
    private String seminar_name;

    @Field(type = FieldType.Text)
    private String seminar_explanation;

}
