package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Wellbeing;
import ru.panfio.telescreen.repository.WellbeingRepository;
import ru.panfio.telescreen.dao.WellbeingDao;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WellbeingService implements Processing {

    private static final int MIN_USAGE_TIME = 5000;
    private static final int MAX_SIZE = 500;

    private final WellbeingRepository wellbeingRepository;
    private final WellbeingDao wellbeingDao;

    /**
     * Constructor.
     *
     * @param wellbeingRepository repo
     * @param wellbeingDao        wellbeingDao
     */
    public WellbeingService(WellbeingRepository wellbeingRepository,
                            WellbeingDao wellbeingDao) {
        this.wellbeingRepository = wellbeingRepository;
        this.wellbeingDao = wellbeingDao;
    }

    /**
     * Saves message records in the database.
     *
     * @param records list of records
     */
    public void saveWellbeingRecords(List<Wellbeing> records) {
        for (Wellbeing wellbeing : records) {
            Wellbeing dbRecord = wellbeingRepository.findByStartTimeAndEndTime(
                    wellbeing.getStartTime(), wellbeing.getEndTime());
            if (dbRecord == null) {
                wellbeingRepository.save(wellbeing);
            }
        }
    }

    /**
     * Finds and returns message records for the period.
     * //TODO rename to app activity
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public List<Wellbeing> getWellbeingBetweenDates(
            Instant from, Instant to) {
        return wellbeingRepository.findByStartTimeBetween(from, to).stream()
                .filter(t -> Duration
                        .between(t.getStartTime(), t.getEndTime())
                        .toMillis() > MIN_USAGE_TIME)
                .collect(Collectors.toList());
    }

    /**
     * Processing App activity from Wellbeing database.
     */
    public void processWellbeingRecords() {
        log.info("Start processing Wellbeing history");
        List<Wellbeing> activities = wellbeingDao.getActivities();

        List<Wellbeing> wellbeingActivities = new ArrayList<>();
        Map<Long, Wellbeing> tmp = new HashMap<>();
        for (Wellbeing activity : activities) {
            if (activity.getType() == 1) {
                tmp.put(activity.getId(), activity);
            }

            if (activity.getType() == 2) {
                final long id = activity.getId();
                Wellbeing tmpRecord = tmp.get(id);
                if (tmpRecord == null) {
                    continue;
                }
                activity.setStartTime(tmpRecord.getStartTime());
                wellbeingActivities.add(activity);
                tmp.remove(id);
            }

            if (wellbeingActivities.size() > MAX_SIZE) {
                saveWellbeingRecords(wellbeingActivities);
                wellbeingActivities.clear();
            }
        }
        saveWellbeingRecords(wellbeingActivities);
        log.info("End processing Wellbeing history");
    }

    @Override
    public void process() {
        processWellbeingRecords();
    }
}
