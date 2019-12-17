package ru.panfio.telescreen.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.panfio.telescreen.util.IsoInstantLocalDateTimeDeserializer;
import ru.panfio.telescreen.util.IsoInstantLocalDateTimeSerializer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
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

}
