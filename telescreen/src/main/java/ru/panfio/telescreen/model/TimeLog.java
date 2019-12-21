package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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