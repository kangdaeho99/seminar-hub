package com.seminarhub.entity;

import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GenericDTORowMapper<T> implements RowMapper<T> {
    private final Class<T> dtoClass;

    public GenericDTORowMapper(Class<T> dtoClass) {
        this.dtoClass = dtoClass;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T dto = null;
        try {
            dto = dtoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dto == null) {
            return null;
        }

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);

            try {
                // DTO의 필드를 동적으로 찾기
                Field field = dtoClass.getDeclaredField(columnName);
                field.setAccessible(true); // private 필드 접근 허용

                // 결과셋에서 컬럼 값 가져오기
                Object value = rs.getObject(columnName);

                // 값이 존재하면 필드에 설정
                if (value != null) {
                    if (field.getType() == char.class || field.getType() == Character.class) {
                        field.set(dto, value.toString().charAt(0));
                    } else if (field.getType() == LocalDateTime.class && value instanceof java.sql.Timestamp) {
                        // Timestamp -> LocalDateTime 변환
                        field.set(dto, ((java.sql.Timestamp) value).toLocalDateTime());
                    } else {
                        field.set(dto, value);
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // DTO에 없는 컬럼은 무시
            }
        }

        return dto;
    }
}