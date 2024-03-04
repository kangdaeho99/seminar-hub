package com.seminarhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
public class SeminarPageResultDTO implements Serializable {
    private Long seminar_no;
    private String seminar_name;
    private String seminar_explanation;
    private Long seminar_price;
    private LocalDateTime inst_dt;
    private LocalDateTime updt_dt;
    private LocalDateTime del_dt;

}
