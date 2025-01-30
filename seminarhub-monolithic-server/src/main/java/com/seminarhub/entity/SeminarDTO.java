package com.seminarhub.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeminarDTO {
    private Long seminar_no;          // 세미나번호 (int unsigned)
    private String name;                 // 이름 (varchar(255))
    private String description;          // 설명 (text)
    private Long price;               // 가격 (int unsigned, default 0)
    private Long available_seats;     // 여석 (int unsigned)
    private LocalDateTime reg_start_date; // 신청시작일자 (datetime)
    private LocalDateTime reg_end_date;   // 신청종료일자 (datetime)
    private LocalDateTime start_date;     // 시작일자 (datetime)
    private LocalDateTime end_date;       // 종료일자 (datetime)
    private String address;              // 주소 (varchar(255), nullable)
    private Long company_no;          // 회사번호 (int unsigned)
    private Long max_capacity;        // 최대인원 (int unsigned)
    private LocalDateTime inst_dt;       // 삽입일시 (datetime)
    private LocalDateTime updt_dt;       // 수정일시 (datetime)
    private LocalDateTime del_dt;        // 삭제일시 (datetime, nullable)
}
