package com.seminarhub.repository;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import static com.querydsl.core.types.OrderSpecifier.NullHandling.Default;

public class OrderByNull extends OrderSpecifier {
    public static final OrderByNull DEFAULT = new OrderByNull();

    private OrderByNull(){
        super(Order.ASC, NullExpression.DEFAULT, Default);
    }
}
