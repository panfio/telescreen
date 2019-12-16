package ru.panfio.telescreen.model.timesheet;

import ru.panfio.telescreen.util.IsoLocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class Task {
    private String taskId;
    private boolean deleted;
    @XmlJavaTypeAdapter(value = IsoLocalDateTimeAdapter.class)
    private LocalDateTime startDate;
    @XmlJavaTypeAdapter(value = IsoLocalDateTimeAdapter.class)
    private LocalDateTime endDate;
    private String description;
    private String location;
    private int feeling;

    public Task() {
    }

    public Task(String taskId, boolean deleted, LocalDateTime startDate, LocalDateTime endDate, String description, String location, int feeling) {
        this.taskId = taskId;
        this.deleted = deleted;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.location = location;
        this.feeling = feeling;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
}
