package com.panfio.telescreen.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.panfio.telescreen.data.util.IsoInstantDeserializer;
import com.panfio.telescreen.data.util.IsoInstantSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class TimeLog {
    @Id
    private String id;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant startDate;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant endDate;
    private String description;
    private String location;
    private int feeling;

    private List<String> tags;
}
