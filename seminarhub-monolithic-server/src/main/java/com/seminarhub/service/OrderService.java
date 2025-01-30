package com.seminarhub.service;

import com.seminarhub.entity.OrderDTO;
import com.seminarhub.exception.SeminarRegistrationFullException;

import java.util.List;

public interface OrderService {
    void placeOrderForSeminars(List<OrderDTO> orderDTOList) throws SeminarRegistrationFullException;
    int createOrder(OrderDTO order);
    OrderDTO getOrderById(int order_no);
    List<OrderDTO> getOrdersByMemberNo(int member_no);
    int updateOrder(OrderDTO order);
    int softDeleteOrder(Long orderNo);
}