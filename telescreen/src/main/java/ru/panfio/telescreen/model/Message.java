package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Message {
    public enum Type { TELEGRAM, SKYPE, WHATSUP, SLACK, VK }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String legacyID;
    private Type type;
    private LocalDateTime created;
    private String author;
    private String content;
}
