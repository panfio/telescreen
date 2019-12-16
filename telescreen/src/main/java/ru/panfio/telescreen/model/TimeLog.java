package ru.panfio.telescreen.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
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

    public TimeLog(String id, LocalDateTime startDate, LocalDateTime endDate,
                   String description, String location, int feeling,
                   List<String> tags) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.location = location;
        this.feeling = feeling;
        this.tags = tags;
    }

    public TimeLog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeLog timeLog = (TimeLog) o;
        return getFeeling() == timeLog.getFeeling() &&
                Objects.equals(getId(), timeLog.getId()) &&
                getStartDate().equals(timeLog.getStartDate()) &&
                getEndDate().equals(timeLog.getEndDate()) &&
                getDescription().equals(timeLog.getDescription()) &&
                Objects.equals(getLocation(), timeLog.getLocation()) &&
                getTags().equals(timeLog.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(), getStartDate(), getEndDate(),
                getDescription(), getLocation(), getFeeling(), getTags());
    }

    @Override
    public String toString() {
        return "TimeLog{" +
                "id='" + id + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", feeling=" + feeling +
                ", tags=" + tags +
                '}';
    }
}
