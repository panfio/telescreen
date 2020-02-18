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
public class Call {
    private final String id;
    private final String number;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private final Instant date;
    private final int duration;
    private final String name;
    private final int type;
}
