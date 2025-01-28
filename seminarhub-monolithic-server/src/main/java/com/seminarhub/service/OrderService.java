package com.seminarhub.service;

import com.seminarhub.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    int createOrder(OrderDTO order);
    OrderDTO getOrderById(int order_no);
    List<OrderDTO> getOrdersByMemberNo(int member_no);
    int updateOrder(OrderDTO order);
    int softDeleteOrder(Long orderNo);
}