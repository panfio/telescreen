package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class ListenRecord {
    public enum Type { SPOTIFY, SOUNDCLOUD }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    private Type type;
    private String artist;
    private String title;
    private LocalDateTime listenTime;
    private String url;

}
