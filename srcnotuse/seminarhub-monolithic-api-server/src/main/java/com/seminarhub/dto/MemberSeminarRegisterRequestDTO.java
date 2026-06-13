package com.seminarhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description : MemberDTO
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSeminarRegisterRequestDTO {
    private String member_id;
    private String seminar_name;

    private Long seminar_no;

    public MemberSeminarRegisterRequestDTO(String memberId, String seminar_name) {
        this.member_id = memberId;
        this.seminar_name = seminar_name;
    }

    public MemberSeminarRegisterRequestDTO(String memberId, Long seminar_no) {
        this.member_id = memberId;
        this.seminar_no = seminar_no;
    }
}
