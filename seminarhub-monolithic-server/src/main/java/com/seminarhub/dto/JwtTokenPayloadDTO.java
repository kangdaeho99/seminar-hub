package com.seminarhub.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
@Builder
@ToString
public class JwtTokenPayloadDTO {
    private Long member_no;
    private String id;

}
