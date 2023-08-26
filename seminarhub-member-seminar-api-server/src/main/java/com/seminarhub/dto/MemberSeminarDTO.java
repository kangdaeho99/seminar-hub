package com.seminarhub.dto;

import com.seminarhub.entity.Seminar;
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
public class MemberSeminarDTO {

    @Schema(description = "member_seminar_no")
    private Long member_seminar_no;

    @Schema(description = "member")
    private String member_id;

    @Schema(description = "seminar")
    private String seminar_name;

    @Schema(description = "inst_dt")
    private LocalDateTime inst_dt;

    @Schema(description = "updt_dt")
    private LocalDateTime updt_dt;

    @Schema(description = "del_dt")
    private LocalDateTime del_dt;

}
