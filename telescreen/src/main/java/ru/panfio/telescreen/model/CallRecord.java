package ru.panfio.telescreen.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CallRecord {
    @Id
    private Long id;
    private String number;
    private LocalDateTime date;
    private int duration;
    private String name;
    private int type;

}
