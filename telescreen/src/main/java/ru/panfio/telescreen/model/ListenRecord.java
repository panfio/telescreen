package ru.panfio.telescreen.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class ListenRecord {
    public enum Type { SPOTIFY, SOUNDCLOUD }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    private Type type;
    private String artist;
    private String title;
    private LocalDateTime listenTime;
    private String url;

    public ListenRecord() {
    }

    public ListenRecord(Long id, String externalId, Type type, String artist,
                        String title, LocalDateTime listenTime, String url) {
        this.id = id;
        this.externalId = externalId;
        this.type = type;
        this.artist = artist;
        this.title = title;
        this.listenTime = listenTime;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getListenTime() {
        return listenTime;
    }

    public void setListenTime(LocalDateTime listenTime) {
        this.listenTime = listenTime;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListenRecord that = (ListenRecord) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getExternalId(), that.getExternalId()) &&
                getType() == that.getType() &&
                Objects.equals(getArtist(), that.getArtist()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getListenTime(), that.getListenTime()) &&
                Objects.equals(getUrl(), that.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getExternalId(), getType(), getArtist(), getTitle(), getListenTime(), getUrl());
    }

    @Override
    public String toString() {
        return "ListenRecord{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", type=" + type +
                ", artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", listenTime=" + listenTime +
                ", url='" + url + '\'' +
                '}';
    }
}
