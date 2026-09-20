package com.seminarhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.seminarhub.dto.SettlementCreateRequest;
import com.seminarhub.dto.SettlementResponse;
import com.seminarhub.dto.SettlementSearchRequest;
import com.seminarhub.dto.SettlementUpdateRequest;
import com.seminarhub.global.annotation.CursorSwagger;
import com.seminarhub.global.annotation.PageableSwagger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class SettlementFormatDocumentationTest {

    @Test
    void declaresNineUniqueDocumentedOperations() {
        Tag tag = SettlementFormatController.class.getAnnotation(Tag.class);
        assertNotNull(tag);
        assertEquals("SettlementFormat", tag.name());

        Set<String> operationIds = Arrays.stream(SettlementFormatController.class.getDeclaredMethods())
                .map(method -> method.getAnnotation(Operation.class))
                .filter(java.util.Objects::nonNull)
                .map(Operation::operationId)
                .collect(Collectors.toSet());
        assertEquals(9, operationIds.size());
    }

    @Test
    void documentsRequestResponseAndSearchSchemas() {
        assertNotNull(SettlementCreateRequest.class.getAnnotation(Schema.class));
        assertNotNull(SettlementUpdateRequest.class.getAnnotation(Schema.class));
        assertNotNull(SettlementSearchRequest.class.getAnnotation(Schema.class));
        assertNotNull(SettlementResponse.class.getAnnotation(Schema.class));
    }

    @Test
    void documentsExplicitPageAndCursorParameters() throws Exception {
        Method page = SettlementFormatController.class.getDeclaredMethod(
                "findPage", SettlementSearchRequest.class, org.springframework.data.domain.Pageable.class);
        Method cursor = SettlementFormatController.class.getDeclaredMethod(
                "findByCursor", SettlementSearchRequest.class, com.seminarhub.global.dto.CursorRequest.class);

        assertNotNull(page.getAnnotation(PageableSwagger.class));
        assertNotNull(cursor.getAnnotation(CursorSwagger.class));
        assertEquals(3, PageableSwagger.class.getAnnotationsByType(Parameter.class).length);
        assertEquals(3, CursorSwagger.class.getAnnotationsByType(Parameter.class).length);
    }
}
