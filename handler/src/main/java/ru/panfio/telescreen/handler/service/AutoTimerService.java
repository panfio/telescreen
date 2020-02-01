package ru.panfio.telescreen.handler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.Autotimer;
import ru.panfio.telescreen.handler.model.autotimer.Activity;
import ru.panfio.telescreen.handler.model.autotimer.AutotimerRecords;
import ru.panfio.telescreen.handler.model.autotimer.TimeEntry;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AutoTimerService implements Processing {
    @Value("${server.zoneOffset}")
    private int zoneOffset;
    private final MessageBus messageBus;
    private final ObjectStorage objectStorage;

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param objectStorage storage
     */
    public AutoTimerService(ObjectStorage objectStorage,
                            MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
    }

    /**
     * Processing AutoTimer records.
     *
     * @return true if success
     */
    public boolean processAutotimerRecords() {
        log.info("Processsing Autotimer records");
        for (String filename : objectStorage.listAllObjects()) {
            if (!filename.contains("activities")) {
                continue;
            }
            //todo processing files in parallel
            List<Activity> activities = parseActivityFile(filename);
            List<Autotimer> list = collectAutotimers(activities);
            list.forEach(activity -> {
                messageBus.send("autotimer", activity);
            });
        }
        return true;
    }


    /**
     * Parsing activity file.
     *
     * @param filename fillename
     * @return activity list
     */
    private List<Activity> parseActivityFile(String filename) {
        try {
            var mapper = new ObjectMapper();
            var autotimerRecords = mapper.readValue(
                    objectStorage.getInputStream(filename),
                    AutotimerRecords.class);
            return autotimerRecords.getActivities();
        } catch (IOException e) {
            log.warn("Autotimer parse error " + filename);
            return new ArrayList<>();
        }
    }

    /**
     * Creates AutoTimer records.
     *
     * @param activities activity
     * @return Autotimer list
     */
    private List<Autotimer> collectAutotimers(List<Activity> activities) {
        List<Autotimer> autotimers = new ArrayList<>();
        for (Activity activity : activities) {
            List<TimeEntry> list = activity.getTime_entries();
            for (TimeEntry timeEntry : list) {
                final String name = activity.getName();
                if (name.startsWith("Desktop")) {
                    continue;
                }
                autotimers.add(createAutotimer(name, timeEntry));
            }
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
    private Autotimer createAutotimer(String name, TimeEntry timeEntry) {
        var autotimer = new Autotimer();
        autotimer.setName(name);
        autotimer.setStartTime(toInstant(timeEntry.getStart_time()));
        autotimer.setEndTime(toInstant(timeEntry.getEnd_time()));
        autotimer.setType(getAutotimerType(name));
        return autotimer;
    }

    /**
     * Converts Date to Instant.
     *
     * @param date date object
     * @return instant
     */
    private Instant toInstant(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneOffset.ofHours(0))
                .toLocalDateTime()
                .toInstant(ZoneOffset.ofHours(zoneOffset));
    }

    /**
     * Extracts Autotimer type based on the activity name.
     *
     * @param name activity name
     * @return Autotimer type
     */
    private int getAutotimerType(final String name) {
        //TODO find alternative
        if (name.startsWith("Google Chrome")
                || name.startsWith("Chromium")
                || name.startsWith("Mozilla Firefox")) {
            return 1;
        }
        if (name.equals("Visual Studio Code")
                || name.startsWith(".../src/main")) {
            return 2;
        }
        if (name.startsWith("Telegram")) {
            //CHECKSTYLE:OFF
            return 3;
            //CHECKSTYLE:ON
        }
        return 0;
    }

    @Override
    public void process() {
        processAutotimerRecords();
    }

    @Override
    public String name() {
        return "autotimer";
    }
}
