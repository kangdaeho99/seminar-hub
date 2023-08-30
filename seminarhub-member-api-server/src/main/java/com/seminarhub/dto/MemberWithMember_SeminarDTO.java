package com.seminarhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * [2023-08-30 daeho.kang]
 * Description: Data Transfer Object (DTO) that links MemberDTO and MemberSeminarDTO.
 * The purpose of this DTO is to facilitate communication between MemberService and Member_SeminarService using openFeign.
 * It encapsulates a MemberDTO along with a list of associated MemberSeminarDTO instances.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberWithMember_SeminarDTO {

    /**
     * [ 2023-08-30 daeho.kang ]
     * Description :
     * The main MemberDTO instance associated with the DTO.
     */
    private MemberDTO memberDTO;

    /**
     * [ 2023-08-30 daeho.kang ]
     * Description :
     * A list of MemberSeminarDTO instances associated with the main MemberDTO.
     */
    private List<MemberSeminarDTO> memberSeminarDTO;

}
