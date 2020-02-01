package com.panfio.telescreen.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.panfio.telescreen.data.util.IsoInstantDeserializer;
import com.panfio.telescreen.data.util.IsoInstantSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

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
