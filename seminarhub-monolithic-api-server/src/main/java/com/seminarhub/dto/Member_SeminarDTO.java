package com.seminarhub.dto;

import com.seminarhub.entity.Member;
import com.seminarhub.entity.Member_Seminar;
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
    private Long member_seminar_no;
    private Long member_no;
    private Long seminar_no;
    private Long member_seminar_payment_history_no;
    private LocalDateTime inst_dt;
    private LocalDateTime updt_dt;
    private LocalDateTime del_dt;
    public Member_Seminar dtoToEntity(){
        return Member_Seminar.builder()
                .member_seminar_no(member_seminar_no)
                .member(new Member(member_no))
                .build();
    }
}
