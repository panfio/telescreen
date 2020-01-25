package ru.panfio.telescreen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Autotimer;
import ru.panfio.telescreen.model.autotimer.Activity;
import ru.panfio.telescreen.model.autotimer.AutotimerRecords;
import ru.panfio.telescreen.model.autotimer.TimeEntry;
import ru.panfio.telescreen.repository.AutotimerRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AutoTimerService implements Processing {
    @Value("${server.zoneOffset}")
    private String zoneOffset;
    private final AutotimerRepository autotimerRepository;
    private final ObjectStorage objectStorage;

    /**
     * Constructor.
     *
     * @param autotimerRepository repo
     * @param objectStorage       repo
     */
    public AutoTimerService(AutotimerRepository autotimerRepository,
                            ObjectStorage objectStorage) {
        this.autotimerRepository = autotimerRepository;
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
            //todo save processed filename item to db ???
            save(list);
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
            ObjectMapper mapper = new ObjectMapper();
            AutotimerRecords autotimerRecords = mapper.readValue(
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
        Autotimer autotimer = new Autotimer();
        autotimer.setName(name);
        autotimer.setStartTime(toInstant(timeEntry.getStart_time()));
        autotimer.setEndTime(toInstant(timeEntry.getEnd_time()));
        autotimer.setType(getAutotimerType(name));
        return autotimer;
    }

    /**
     * Converts Date to Instant.
     * @param date date object
     * @return instant
     */
    private Instant toInstant(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneOffset.ofHours(0))
                .toLocalDateTime()
                .toInstant(ZoneOffset.ofHours(Integer.parseInt(zoneOffset)));
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

    /**
     * Saves AutoTimer records in the database.
     *
     * @param records list of records
     */
    public void save(List<Autotimer> records) {
        autotimerRepository.saveAll(records);
    }

    /**
     * Finds and returns the "AutoTimer" records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<Autotimer> getAutotimerRecordsBetweenDates(
            Instant from, Instant to) {
        return autotimerRepository.findByStartTimeBetween(from, to)
                .stream().filter(t -> {
                    long duration = Duration.between(
                            t.getStartTime(), t.getEndTime()).toMillis();
                    // 10s < duration < 5h
                    //CHECKSTYLE:OFF
                    return duration > 10000 && duration < 18000000;
                    //CHECKSTYLE:ON
                }).collect(Collectors.toList());
    }

    @Override
    public void process() {
        processAutotimerRecords();
    }
}
