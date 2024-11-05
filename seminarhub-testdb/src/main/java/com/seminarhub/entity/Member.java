package com.seminarhub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Long memberNo; // 회원번호
    private String id; // 아이디
    private String name; // 이름
    private String pw; // 패스워드
    private char gender; // 성별
    private int age; // 나이
    private String address; // 주소
    private String job; // 직업
    private String grade; // 등급
    private int points; // 적립금
    private LocalDateTime instDt; // 삽입일시
    private LocalDateTime updtDt; // 수정일시
    private LocalDateTime delDt; // 삭제일시

}
