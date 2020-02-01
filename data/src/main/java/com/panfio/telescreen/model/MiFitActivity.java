package com.panfio.telescreen.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.panfio.telescreen.data.util.IsoInstantDeserializer;
import com.panfio.telescreen.data.util.IsoInstantSerializer;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
public class MiFitActivity {

    @Id
    private String id;

    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant date;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant sleepStart;
    @JsonSerialize(using = IsoInstantSerializer.class)
    @JsonDeserialize(using = IsoInstantDeserializer.class)
    private Instant sleepEnd;
    private int inBedMin;
    private int DeepSleepMin;
    private int LightSleepMin;
    private int AwakeMin;
    private int DailyDistanceMeter;
    private int DailySteps;
    private int DailyBurnCalories;
    private int WalkDistance;
    private int WalkTimeMin;
    private int WalkBurnCalories;
    private int RunDistanceMeter;
    private int RunTimeMin;
    private int RunBurnCalories;
}
