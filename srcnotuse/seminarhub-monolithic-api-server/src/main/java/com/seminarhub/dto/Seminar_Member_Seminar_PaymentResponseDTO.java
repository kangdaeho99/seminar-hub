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
public class Seminar_Member_Seminar_PaymentResponseDTO implements Serializable {
    private Long seminar_no;
    private String seminar_name;
    private String seminar_explanation;
    private Long seminar_price;
    private LocalDateTime seminar_inst_dt;
    private LocalDateTime seminar_updt_dt;
    private LocalDateTime seminar_del_dt;
    private Long member_seminar_no;
    private Long member_no;
    private LocalDateTime member_inst_dt;
    private LocalDateTime member_updt_dt;
    private LocalDateTime member_del_dt;
    private Long payment_no;
    private Long amount;
    private LocalDateTime payment_inst_dt;
    private LocalDateTime payment_updt_dt;
    private LocalDateTime payment_del_dt;
}
