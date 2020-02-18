package ru.panfio.telescreen.handler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import ru.panfio.telescreen.handler.util.IsoInstantDeserializer;
import ru.panfio.telescreen.handler.util.IsoInstantSerializer;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class TimeLog {
    private final String id;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private final Instant startDate;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private final Instant endDate;
    private final String description;
    private final String location;
    private final int feeling;
    private final List<String> tags;
}
