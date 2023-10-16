package com.seminarhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "seminar_no")
    private Long seminar_no;

    @Schema(description = "seminar_name")
    private String seminar_name;

    @Schema(description = "seminar_explanation")
    private String seminar_explanation;

    @Schema(description = "seminar_explanation")
    private Long seminar_price;

    @Schema(description = "inst_dt")
    private LocalDateTime seminar_inst_dt;

    @Schema(description = "updt_dt")
    private LocalDateTime seminar_updt_dt;

    @Schema(description = "del_dt")
    private LocalDateTime seminar_del_dt;

    @Schema(description = "member_seminar_no")
    private Long member_seminar_no;

    @Schema(description = "member_no")
    private Long member_no;

    @Schema(description = "member_inst_dt")
    private LocalDateTime member_inst_dt;

    @Schema(description = "member_updt_dt")
    private LocalDateTime member_updt_dt;

    @Schema(description = "member_del_dt")
    private LocalDateTime member_del_dt;

    @Schema(description = "payment_no")
    private Long payment_no;

    @Schema(description = "amount")
    private Long amount;

    @Schema(description = "payment_inst_dt")
    private LocalDateTime payment_inst_dt;

    @Schema(description = "payment_updt_dt")
    private LocalDateTime payment_updt_dt;

    @Schema(description = "payment_del_dt")
    private LocalDateTime payment_del_dt;


}
