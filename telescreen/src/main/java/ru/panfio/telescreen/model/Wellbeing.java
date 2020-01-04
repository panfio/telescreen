package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@ToString
public class Wellbeing {
    @Id
    private Long id;
    private int type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String app;
}
