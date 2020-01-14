package ru.panfio.telescreen.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class MiFitActivity {

    @Id
    private String id;

    private LocalDateTime date;
    private LocalDateTime SleepStart;
    private LocalDateTime SleepEnd;
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
