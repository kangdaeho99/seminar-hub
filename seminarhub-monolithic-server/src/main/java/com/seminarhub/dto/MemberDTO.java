package com.seminarhub.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDTO {
        private Long member_no;     // 회원번호 (int unsigned)
        private String id;             // 아이디 (varchar(40))
        private String name;           // 이름 (varchar(20))
        private String pw;             // 패스워드 (varchar(64))
        private String gender;      // 성별 (char(1))
        private Long age;           // 나이 (int unsigned)
        private String address;        // 주소 (varchar(255), nullable)
        private String job;            // 직업 (varchar(30), nullable)
        private String grade;          // 등급 (varchar(10), default 'bronze')
        private Long points;        // 적립금 (int unsigned, default 0)
        private LocalDateTime inst_dt; // 삽입일시 (datetime)
        private LocalDateTime updt_dt; // 수정일시 (datetime)
        private LocalDateTime del_dt;  // 삭제일시 (datetime, nullable)
}