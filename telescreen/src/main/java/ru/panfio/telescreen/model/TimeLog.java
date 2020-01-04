package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TimeLog {
    @Id
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private String location;
    private int feeling;

    private List<String> tags;
}
