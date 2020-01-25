package ru.panfio.telescreen.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
public class MiFitActivity {

    @Id
    private String id;

    private Instant date;
    private Instant SleepStart;
    private Instant SleepEnd;
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
