package ru.panfio.telescreen.handler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

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
}
