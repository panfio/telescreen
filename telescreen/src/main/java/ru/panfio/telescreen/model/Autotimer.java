package ru.panfio.telescreen.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(indexes = { @Index(name = "IDX_AUTOTIMER", columnList = "startTime,endTime") })
public class Autotimer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 65535,columnDefinition="Text")
    private String name;
    private int type;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime endTime;

    public Autotimer() {
    }

    public Autotimer(Long id, String name, int type, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autotimer autotimer = (Autotimer) o;
        return getType() == autotimer.getType() &&
                Objects.equals(getId(), autotimer.getId()) &&
                Objects.equals(getName(), autotimer.getName()) &&
                Objects.equals(getStartTime(), autotimer.getStartTime()) &&
                Objects.equals(getEndTime(), autotimer.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getType(), getStartTime(), getEndTime());
    }
}
