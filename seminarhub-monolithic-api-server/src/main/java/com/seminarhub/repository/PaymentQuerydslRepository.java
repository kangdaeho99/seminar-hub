package com.seminarhub.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PaymentQuerydslRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;


}
