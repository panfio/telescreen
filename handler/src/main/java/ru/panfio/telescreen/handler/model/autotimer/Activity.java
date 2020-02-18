package ru.panfio.telescreen.handler.model.autotimer;

import lombok.Data;
import ru.panfio.telescreen.handler.model.Autotimer;
import ru.panfio.telescreen.handler.service.util.DateWizard;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Activity {
    private String name;
    private List<TimeEntry> time_entries;

    public List<Autotimer> collectAutotimers() {
        return time_entries
                .stream()
                .map(timeEntry -> createAutotimer(name, timeEntry))
                .collect(Collectors.toList());
    }

    private Autotimer createAutotimer(String name,
                                      TimeEntry timeEntry) {
        return Autotimer.builder()
                .name(name)
                .type(Autotimer.typeByName(name))
                .startTime(toInstant(timeEntry.getStart_time()))
                .endTime(toInstant(timeEntry.getEnd_time()))
                .build();
    }

    private Instant toInstant(Date date) {
        var local = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneOffset.ofHours(0))
                .toLocalDateTime();
        return DateWizard.toInstant(local);
    }

}
