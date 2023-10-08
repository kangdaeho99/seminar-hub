package com.seminarhub.dto;

import com.seminarhub.entity.Member;
import com.seminarhub.entity.Member_Seminar;
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
public class Member_SeminarDTO {

    @Schema(description = "member_seminar_no")
    private Long member_seminar_no;

    @Schema(description = "member")
    private Long member_no;

    @Schema(description = "seminar")
    private Long seminar_no;

    @Schema(description = "inst_dt")
    private LocalDateTime inst_dt;

    @Schema(description = "updt_dt")
    private LocalDateTime updt_dt;

    @Schema(description = "del_dt")
    private LocalDateTime del_dt;

    public Member_Seminar dtoToEntity(){
        return Member_Seminar.builder()
                .member_seminar_no(member_seminar_no)
                .member(new Member(member_no))
                .build();
    }
}
