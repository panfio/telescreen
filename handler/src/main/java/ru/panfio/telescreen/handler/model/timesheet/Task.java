package ru.panfio.telescreen.handler.model.timesheet;

import lombok.Data;
import ru.panfio.telescreen.handler.util.IsoLocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Instant;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Task {
    private String taskId;
    private boolean deleted;
    @XmlJavaTypeAdapter(value = IsoLocalDateTimeAdapter.class)
    private Instant startDate;
    @XmlJavaTypeAdapter(value = IsoLocalDateTimeAdapter.class)
    private Instant endDate;
    private String description;
    private String location;
    private int feeling;
}
