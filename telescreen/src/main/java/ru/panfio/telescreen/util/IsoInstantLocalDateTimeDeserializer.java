package ru.panfio.telescreen.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class IsoInstantLocalDateTimeDeserializer
        extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(
            JsonParser jp, DeserializationContext dc) throws IOException {
        ObjectCodec codec = jp.getCodec();
        TextNode node = codec.readTree(jp);
        String dateString = node.textValue();
        Instant instant = Instant.parse(dateString);
        LocalDateTime dateTime =
                LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime;
    }
}
