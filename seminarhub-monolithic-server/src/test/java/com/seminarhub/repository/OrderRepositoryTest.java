package com.seminarhub.repository;


import com.seminarhub.entity.OrderDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    public OrderRepository orderRepository;

    // CREATE 테스트
    @Test
    public void testInsertOrder() {
        OrderDTO order = new OrderDTO();
        order.setMember_no(1L); // 테스트에 맞는 실제 member_no 값으로 설정
        order.setSeminar_no(10L); // 테스트에 맞는 실제 seminar_no 값으로 설정
        order.setQuantity(2L);

        int result = orderRepository.insert(order);
        Assertions.assertEquals(1, result, "Order should be saved successfully.");
    }

    // SELECT 테스트
    @Test
    public void testFindOrderById() {
        int orderNo = 100; // 테스트에 맞는 실제 order_no 값으로 변경
        OrderDTO order = orderRepository.findOrderById(orderNo);
        Assertions.assertNotNull(order, "Order should not be null.");
        System.out.println(order);
    }

    // SELECT LIST 테스트 (회원별 주문 조회)
    @Test
    public void testFindOrdersByMemberNo() {
        int memberNo = 1; // 테스트에 맞는 실제 member_no 값으로 변경
        List<OrderDTO> orders = orderRepository.findOrdersByMemberNo(memberNo);
        Assertions.assertFalse(orders.isEmpty(), "Orders list should not be empty.");
        orders.forEach(System.out::println);
    }

    // UPDATE 테스트
    @Test
    public void testUpdateOrder() {
        OrderDTO order = new OrderDTO();
        order.setOrder_no(100L); // 테스트에 맞는 실제 order_no 값으로 변경
        order.setMember_no(1L); // 테스트에 맞는 실제 member_no 값으로 설정
        order.setSeminar_no(20L); // 업데이트할 seminar_no 값 설정
        order.setQuantity(5L); // 업데이트할 주문량 설정

        int result = orderRepository.update(order);
        Assertions.assertEquals(1, result, "Order should be updated successfully.");
    }

    // DELETE (Soft Delete) 테스트
    @Test
    public void testSoftDeleteOrder() {
        Long orderNo = 100L; // 테스트에 맞는 실제 order_no 값으로 변경
        int result = orderRepository.softDelete(orderNo);
        Assertions.assertEquals(1, result, "Soft delete should be successful.");
    }
}