package ru.panfio.telescreen.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class CallRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private LocalDateTime date;
    private int duration;
    private String name;
    private int type;

    public CallRecord(Long id, String number, LocalDateTime date,
                      int duration, String name, int type) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.duration = duration;
        this.name = name;
        this.type = type;
    }

    public CallRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallRecord that = (CallRecord) o;
        return duration == that.duration &&
                type == that.type &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getNumber(), that.getNumber()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumber(), getDate(), duration, getName(), type);
    }

    @Override
    public String toString() {
        return "CallRecord{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
