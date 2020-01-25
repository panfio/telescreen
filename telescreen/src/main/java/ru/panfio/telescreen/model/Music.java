package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Music implements Cloneable {
    public enum Type {SPOTIFY, SOUNDCLOUD}

    @Id
    private Long id;
    private String externalId;
    private Type type;
    private String artist;
    private String title;
    private LocalDateTime listenTime;
    private String url;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
