package com.seminarhub.dto;

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
public class Member_Seminar_Payment_HistoryDTO {
    private Long member_seminar_payment_history_no;
    private Long member_seminar_payment_history_amount;
    private LocalDateTime inst_dt;
    private LocalDateTime updt_dt;
    private LocalDateTime del_dt;
}
