package ru.panfio.telescreen.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import ru.panfio.telescreen.util.IsoInstantDeserializer;
import ru.panfio.telescreen.util.IsoInstantSerializer;

import java.io.Serializable;
import java.time.Instant;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Wellbeing implements Serializable {
    @Id
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
