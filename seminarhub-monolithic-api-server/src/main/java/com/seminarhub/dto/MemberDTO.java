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
public class MemberDTO {
    private Long member_no;
    private String member_id;
    private String member_password;
    private String member_nickname;
    private boolean member_from_social;
    private LocalDateTime inst_dt;
    private LocalDateTime updt_dt;
    private LocalDateTime del_dt;

}
