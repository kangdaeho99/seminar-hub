package com.seminarhub.controller;

import com.seminarhub.annotation.CurrentUser;
import com.seminarhub.annotation.UseGuard;
import com.seminarhub.entity.MemberDTO;
import com.seminarhub.entity.OrderDTO;
import com.seminarhub.service.MemberService;
import com.seminarhub.service.OrderService;
import com.seminarhub.service.SeminarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final SeminarService seminarService;

    public OrderController(OrderService orderService, MemberService memberService, SeminarService seminarService) {
        this.orderService = orderService;
        this.memberService = memberService;
        this.seminarService = seminarService;
    }

    //Create Order Service
    @UseGuard()
    @PostMapping("")
    public ResponseEntity<String> buySeminarWithShoopingCart(@CurrentUser MemberDTO memberDTO, @RequestBody List<OrderDTO> orderDTOList){
        orderService.placeOrderForSeminars(orderDTOList);
        return ResponseEntity.ok("Order created successfuly");
    }

    // Create order
    @UseGuard()
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO order) {
        int result = orderService.createOrder(order);
        if (result > 0) {
            return ResponseEntity.ok("Order created successfully");
        }
        return ResponseEntity.status(500).body("Failed to create order");
    }

    // Get order by ID
    @UseGuard()
    @GetMapping("/{order_no}")
    public ResponseEntity<OrderDTO> getOrderById(@CurrentUser() Object user, @PathVariable int order_no) {
        OrderDTO order = orderService.getOrderById(order_no);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Get orders by member_no
    @GetMapping("/member/{member_no}")
    public ResponseEntity<List<OrderDTO>> getOrdersByMemberNo(@PathVariable int member_no) {
        List<OrderDTO> orders = orderService.getOrdersByMemberNo(member_no);
        if (!orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Update order
    @PutMapping("/{order_no}")
    public ResponseEntity<String> updateOrder(@PathVariable Long order_no, @RequestBody OrderDTO order) {
        order.setOrder_no(order_no); // Ensure the order_no is set in the DTO
        int result = orderService.updateOrder(order);
        if (result > 0) {
            return ResponseEntity.ok("Order updated successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to update order");
        }
    }

    // Soft delete order
    @DeleteMapping("/{order_no}")
    public ResponseEntity<String> softDeleteOrder(@PathVariable Long order_no) {
        int result = orderService.softDeleteOrder(order_no);
        if (result > 0) {
            return ResponseEntity.ok("Order deleted successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to delete order");
        }
    }
}