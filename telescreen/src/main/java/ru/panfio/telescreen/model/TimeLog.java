package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class TimeLog {
    @Id
    private String id;
    private Instant startDate;
    private Instant endDate;
    private String description;
    private String location;
    private int feeling;

    private List<String> tags;
}
