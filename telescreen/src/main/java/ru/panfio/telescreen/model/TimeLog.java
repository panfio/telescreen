package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
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
    @ElementCollection
    @CollectionTable(name = "TIMELOG_TAG")
    @OrderColumn
    @Column(name = "TAG")
    private List<String> tags;
}
