package com.seminarhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description : MemberDTO
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long payment_no;
    private Long member_seminar_no;
    private Long amount;
    private LocalDateTime inst_dt;
    private LocalDateTime updt_dt;
    private LocalDateTime del_dt;

}
