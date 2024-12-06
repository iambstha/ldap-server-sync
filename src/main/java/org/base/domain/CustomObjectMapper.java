package org.base.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.io.IOException;

@Getter
@ApplicationScoped
public class CustomObjectMapper {

    private final ObjectMapper objectMapper;

    public CustomObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public String writeValueAsString(Object value) throws IOException {
        return objectMapper.writeValueAsString(value);
    }
}
