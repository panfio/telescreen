package ru.panfio.telescreen.handler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import ru.panfio.telescreen.handler.util.IsoInstantDeserializer;
import ru.panfio.telescreen.handler.util.IsoInstantSerializer;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Media {
    @Id
    private String id;
    private String type;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant created;
    private String path;
}