package ru.panfio.telescreen.handler.model.autotimer;

import lombok.Data;
import ru.panfio.telescreen.handler.model.Autotimer;
import ru.panfio.telescreen.handler.service.util.DateWizard;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Activity {
    private String name;
    private List<TimeEntry> time_entries;

    public List<Autotimer> collectAutotimers() {
        List<Autotimer> autotimers = new ArrayList<>();
        for (TimeEntry timeEntry : time_entries) {
            autotimers.add(createAutotimer(name, timeEntry));
        }
        return autotimers;
    }

    /**
     * Creates the Autotimer entity.
     *
     * @param name      activity name
     * @param timeEntry time entry
     * @return autotimer
     */
    private Autotimer createAutotimer(String name,
                                      TimeEntry timeEntry) {
        return Autotimer.builder()
                .name(name)
                .type(Autotimer.typeByName(name))
                .startTime(toInstant(timeEntry.getStart_time()))
                .endTime(toInstant(timeEntry.getEnd_time()))
                .build();
    }

    /**
     * Converts Date to Instant.
     *
     * @param date date object
     * @return instant
     */
    private Instant toInstant(Date date) {
        LocalDateTime ldt = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneOffset.ofHours(0))
                .toLocalDateTime();
        return DateWizard.toInstant(ldt);
    }

}
