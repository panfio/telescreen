package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
public class Wellbeing {
    public enum Type { ACTIVITY, NOTIFICATION }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Type type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String app;
}