package com.seminarhub.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminarhub.error.ConflictException;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void serializesOnlyTheCommonErrorContract() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(new ConflictException("conflict")));

        assertThat(json.get("origin").asText()).isEqualTo("COMMON");
        assertThat(json.get("code").asInt()).isEqualTo(409);
        assertThat(json.get("message").asText()).isEqualTo("conflict");
        assertThat(json.has("stackTrace")).isFalse();
    }
}
