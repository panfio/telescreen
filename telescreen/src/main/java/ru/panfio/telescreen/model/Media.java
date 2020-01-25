package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Media {
    @Id
    private String id;
    private String type;
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Instant created;
    private String path;
}
