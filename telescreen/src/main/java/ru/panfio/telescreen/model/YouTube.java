package ru.panfio.telescreen.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.panfio.telescreen.util.IsoInstantLocalDateTimeDeserializer;
import ru.panfio.telescreen.util.IsoInstantLocalDateTimeSerializer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTube {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @JsonAlias("titleUrl")
    private String url;
    @JsonSerialize(using = IsoInstantLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IsoInstantLocalDateTimeDeserializer.class)
    private LocalDateTime time;

    public YouTube(Long id, String title, String url, LocalDateTime time) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.time = time;
    }

    public YouTube() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YouTube youTube = (YouTube) o;
        return getTitle().equals(youTube.getTitle()) &&
                getUrl().equals(youTube.getUrl()) &&
                getTime().equals(youTube.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getUrl(), getTime());
    }

    @Override
    public String toString() {
        return "YouTube{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", time=" + time +
                '}';
    }
}
