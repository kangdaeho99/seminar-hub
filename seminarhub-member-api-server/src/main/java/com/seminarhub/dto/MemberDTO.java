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
public class MemberDTO {

    @Schema(description = "member_no")
    private Long member_no;

    @Schema(description = "member_id", format="email")
    private String member_id;
    @Schema(description = "member_password")
    private String member_password;

    @Schema(description = "member_nickname")
    private String member_nickname;

    @Schema(description = "member_from_social")
    private boolean member_from_social;

    @Schema(description = "inst_dt")
    private LocalDateTime inst_dt;

    @Schema(description = "updt_dt")
    private LocalDateTime updt_dt;

    @Schema(description = "del_dt")
    private LocalDateTime del_dt;

}
