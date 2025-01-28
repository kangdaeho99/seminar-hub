package com.seminarhub.service;

import com.seminarhub.dto.OrderDTO;
import com.seminarhub.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public int createOrder(OrderDTO order) {
        return orderRepository.insert(order);
    }

    @Override
    public OrderDTO getOrderById(int order_no) {
        return orderRepository.findOrderById(order_no);
    }

    @Override
    public List<OrderDTO> getOrdersByMemberNo(int member_no) {
        return orderRepository.findOrdersByMemberNo(member_no);
    }

    @Override
    public int updateOrder(OrderDTO order) {
        return orderRepository.update(order);
    }

    @Override
    public int softDeleteOrder(Long orderNo) {
        return orderRepository.softDelete(orderNo);
    }
}