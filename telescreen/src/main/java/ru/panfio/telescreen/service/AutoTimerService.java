package ru.panfio.telescreen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Autotimer;
import ru.panfio.telescreen.model.autotimer.Activity;
import ru.panfio.telescreen.model.autotimer.AutotimerRecords;
import ru.panfio.telescreen.model.autotimer.TimeEntry;
import ru.panfio.telescreen.repository.AutotimerRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AutoTimerService implements Processing {

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
        int count = 0;
        for (String filename : objectStorage.listAllObjects()) {
            if (!filename.contains("activities")) {
                continue;
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                AutotimerRecords autotimerRecords = mapper.readValue(
                        objectStorage.getInputStream(filename),
                        AutotimerRecords.class);
                List<Autotimer> list =
                        collectAutotimers(autotimerRecords.getActivities());
                count = count + list.size();
                //todo save processed filename item to db
                save(list);
            } catch (IOException e) {
                log.warn("Autotimer parse error " + filename);
                e.printStackTrace();
                return false;
            }
        }
        log.info("Processed Autotimer items = " + count);
        return true;
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
                String name = activity.getName();
                if (name.startsWith("Desktop")) {
                    continue;
                }
                Autotimer autotimer = new Autotimer();
                autotimer.setName(name);
                autotimer.setStartTime(timeEntry.getStart_time().toInstant()
                        .atOffset(ZoneOffset.ofHours(0)).toLocalDateTime());
                autotimer.setEndTime(timeEntry.getEnd_time().toInstant()
                        .atOffset(ZoneOffset.ofHours(0)).toLocalDateTime());
                autotimer.setType(0); //default
                //TODO find alternative
                if (name.startsWith("Google Chrome")
                        || name.startsWith("Chromium")
                        || name.startsWith("Mozilla Firefox")) {
                    autotimer.setType(1);
                }
                if (name.equals("Visual Studio Code")
                        || name.startsWith(".../src/main")) {
                    autotimer.setType(2);
                }
                if (name.startsWith("Telegram")) {
                    //CHECKSTYLE:OFF
                    autotimer.setType(3);
                    //CHECKSTYLE:ON
                }
                autotimers.add(autotimer);
            }
        }
        return autotimers;
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
            LocalDateTime from, LocalDateTime to) {
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
