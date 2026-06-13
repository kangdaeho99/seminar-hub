package com.seminarhub.dto;

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
    private Long seminar_no;
    private String seminar_name;
    private String seminar_explanation;
    private Long seminar_price;
    private Long seminar_max_participants;
    private Long seminar_participants_cnt;
    private Timestamp inst_dt;
    private Timestamp updt_dt;
    private LocalDateTime del_dt;
}
