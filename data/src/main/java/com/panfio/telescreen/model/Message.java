package com.panfio.telescreen.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.panfio.telescreen.data.util.IsoInstantDeserializer;
import com.panfio.telescreen.data.util.IsoInstantSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Message {
    public enum Type { TELEGRAM, SKYPE, WHATSUP, SLACK, VK }
    @Id
    private String id;
    private String legacyID;
    private Type type;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant created;
    private String author;

    private String content;
}
