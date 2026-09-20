package com.seminarhub.global.dto;

public enum Direction {
    ASC,
    DESC;

    public boolean isAscending() {
        return this == ASC;
    }
}
