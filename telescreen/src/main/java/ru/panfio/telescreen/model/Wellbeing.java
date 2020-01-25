package ru.panfio.telescreen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Wellbeing {
    @Id
    private Long id;
    private int type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String app;
}
