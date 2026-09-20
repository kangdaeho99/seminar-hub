package com.seminarhub.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void okWithoutDataUsesTheCommonResponseShape() throws Exception {
        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(ApiResponse.ok()));

        assertThat(json.get("status").asInt()).isEqualTo(200);
        assertThat(json.get("message").asText()).isEqualTo("OK");
        assertThat(json.get("data").isObject()).isTrue();
        assertThat(json.get("data").isEmpty()).isTrue();
    }

    @Test
    void okWithDataPreservesThePayload() {
        ApiResponse<Map<String, Long>> response = ApiResponse.ok(Map.of("id", 1L));

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getData()).containsEntry("id", 1L);
    }
}
