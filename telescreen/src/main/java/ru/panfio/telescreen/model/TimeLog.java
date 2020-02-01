package ru.panfio.telescreen.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import ru.panfio.telescreen.util.IsoInstantDeserializer;
import ru.panfio.telescreen.util.IsoInstantSerializer;

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
