package com.seminarhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeminarDTO {

    @Schema(description = "seminar_no")
    private Long seminar_no;

    @Schema(description = "seminar_name")
    private String seminar_name;

    @Schema(description = "seminar_explanation")
    private String seminar_explanation;


    @Schema(description = "inst_dt")
    private LocalDateTime inst_dt;

    @Schema(description = "updt_dt")
    private LocalDateTime updt_dt;

    @Schema(description = "del_dt")
    private LocalDateTime del_dt;

}
