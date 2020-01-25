package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Autotimer {

    @Id
    private String id;
    private String name;
    private int type;
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Instant startTime;
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Instant endTime;

}
