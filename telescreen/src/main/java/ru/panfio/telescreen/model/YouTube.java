package ru.panfio.telescreen.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import ru.panfio.telescreen.util.IsoInstantLocalDateTimeDeserializer;
import ru.panfio.telescreen.util.IsoInstantLocalDateTimeSerializer;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTube {
    @Id
    private Long id;
    private String title;
    @JsonAlias("titleUrl")
    private String url;
    @JsonSerialize(using = IsoInstantLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IsoInstantLocalDateTimeDeserializer.class)
    private LocalDateTime time;
}
