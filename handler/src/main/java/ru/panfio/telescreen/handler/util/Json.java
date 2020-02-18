package ru.panfio.telescreen.handler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public final class Json {
    private Json() {

    }
    /**
     * Convert object to json string.
     * @param object object
     * @return json
     */
    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    public static <T> T parse(String json, Class<T> convertClass) {
        try {
            var mapper = new ObjectMapper();
            return (T) mapper.readValue(json, convertClass);
        } catch (IOException e) {
            throw new RuntimeException("Parse Error" + e.getMessage());
        }
    }
}
