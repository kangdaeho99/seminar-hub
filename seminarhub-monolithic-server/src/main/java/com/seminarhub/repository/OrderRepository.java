package com.seminarhub.repository;


import com.seminarhub.dto.GenericDTORowMapper;
import com.seminarhub.dto.OrderDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Create (Insert)
    public int insert(OrderDTO order) {
        String sql = """
            INSERT INTO orders (member_no, seminar_no, quantity, inst_dt, updt_dt) 
            VALUES (?, ?, ?, ?, ?)
        """;
        return jdbcTemplate.update(sql,
                order.getMember_no(),
                order.getSeminar_no(),
                order.getQuantity(),
                LocalDateTime.now(),  // inst_dt
                LocalDateTime.now()   // updt_dt
        );
    }

    // Read (Select by Order No)
    public OrderDTO findOrderById(int order_no) {
        String sql = "SELECT * FROM orders WHERE order_no = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{order_no}, new GenericDTORowMapper<>(OrderDTO.class));
    }

    // Read (Select List by Member No)
    public List<OrderDTO> findOrdersByMemberNo(int member_no) {
        String sql = "SELECT * FROM orders WHERE member_no = ?";
        return jdbcTemplate.query(sql, new Object[]{member_no}, new GenericDTORowMapper<>(OrderDTO.class));
    }

    // Update
    public int update(OrderDTO order) {
        String sql = """
            UPDATE orders 
            SET member_no = ?, seminar_no = ?, quantity = ?, updt_dt = ? 
            WHERE order_no = ?
        """;
        return jdbcTemplate.update(sql,
                order.getMember_no(),
                order.getSeminar_no(),
                order.getQuantity(),
                LocalDateTime.now(),  // updt_dt
                order.getOrder_no()
        );
    }

    // Soft Delete
    public int softDelete(Long orderNo) {
        String sql = "UPDATE orders SET del_dt = ? WHERE order_no = ?";
        return jdbcTemplate.update(sql, LocalDateTime.now(), orderNo);
    }
}
