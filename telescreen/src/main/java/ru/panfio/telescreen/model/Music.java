package ru.panfio.telescreen.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import ru.panfio.telescreen.util.IsoInstantDeserializer;
import ru.panfio.telescreen.util.IsoInstantSerializer;

import java.time.Instant;

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
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant listenTime;
    private String url;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
