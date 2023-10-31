package com.seminarhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * [ 2023-08-21 daeho.kang ]
 * Description : SeminarDTO
 *
 */
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

    @Schema(description = "seminar_price")
    private Long seminar_price;

    @Schema(description = "seminar_maxParticipants")
    private Long seminar_max_participants;

    @Schema(description = "inst_dt")
    private Timestamp inst_dt;

    @Schema(description = "updt_dt")
    private Timestamp updt_dt;

    @Schema(description = "del_dt")
    private LocalDateTime del_dt;

}
