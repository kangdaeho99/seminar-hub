package com.seminarhub.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long order_no;         // 주문번호 (int unsigned)
    private Long member_no;        // 회원번호 (int unsigned)
    private Long seminar_no;       // 세미나번호 (int unsigned)
    private Long quantity;      // 주문량 (int unsigned)
    private LocalDateTime inst_dt; // 삽입일시 (datetime)
    private LocalDateTime updt_dt; // 수정일시 (datetime)
    private LocalDateTime del_dt;  // 삭제일시 (datetime, nullable)

    public OrderDTO(){

    }
    public OrderDTO(Long member_no, Long seminar_no, Long quantity){
        this.member_no = member_no;
        this.seminar_no = seminar_no;
        this.quantity = quantity;
    }

}
