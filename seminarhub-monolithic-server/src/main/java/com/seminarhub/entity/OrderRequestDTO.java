package com.seminarhub.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    List<OrderDTO> orderDTOList;
}
