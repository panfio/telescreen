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
public class Call {
    @Id
    private String id;
    private String number;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant date;
    private int duration;
    private String name;
    private int type;

}
