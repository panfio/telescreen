package ru.panfio.telescreen.handler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import ru.panfio.telescreen.handler.util.IsoInstantDeserializer;
import ru.panfio.telescreen.handler.util.IsoInstantSerializer;

import java.time.Instant;

@Getter
@Builder
public class Message {
    public enum Type { TELEGRAM, SKYPE, WHATSUP, SLACK, VK }
    private final String id;
    private final String legacyID;
    private final Type type;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private final Instant created;
    private final String author;
    private final String content;

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", legacyID='" + legacyID + '\'' +
                ", type=" + type +
                ", created=" + created +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
