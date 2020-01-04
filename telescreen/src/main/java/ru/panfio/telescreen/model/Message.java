package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Message {
    public enum Type { TELEGRAM, SKYPE, WHATSUP, SLACK, VK }
    @Id
    private String id;
    private String legacyID;
    private Type type;
    private LocalDateTime created;
    private String author;

    private String content;
}
