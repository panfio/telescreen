package ru.panfio.telescreen.handler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.panfio.telescreen.handler.util.IsoInstantDeserializer;
import ru.panfio.telescreen.handler.util.IsoInstantSerializer;

import java.time.Instant;

@Data
public class MiFitActivity {
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

    @Override
    public String toString() {
        return "MiFitActivity{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", sleepStart=" + sleepStart +
                ", sleepEnd=" + sleepEnd +
                ", inBedMin=" + inBedMin +
                ", DeepSleepMin=" + DeepSleepMin +
                ", LightSleepMin=" + LightSleepMin +
                ", AwakeMin=" + AwakeMin +
                ", DailyDistanceMeter=" + DailyDistanceMeter +
                ", DailySteps=" + DailySteps +
                ", DailyBurnCalories=" + DailyBurnCalories +
                ", WalkDistance=" + WalkDistance +
                ", WalkTimeMin=" + WalkTimeMin +
                ", WalkBurnCalories=" + WalkBurnCalories +
                ", RunDistanceMeter=" + RunDistanceMeter +
                ", RunTimeMin=" + RunTimeMin +
                ", RunBurnCalories=" + RunBurnCalories +
                '}';
    }
}
