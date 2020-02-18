package ru.panfio.telescreen.handler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import ru.panfio.telescreen.handler.util.IsoInstantDeserializer;
import ru.panfio.telescreen.handler.util.IsoInstantSerializer;

import java.io.Serializable;
import java.time.Instant;

@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Wellbeing implements Serializable {
    private Long id;
    private int type;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant startTime;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant endTime;
    private String app;
}
