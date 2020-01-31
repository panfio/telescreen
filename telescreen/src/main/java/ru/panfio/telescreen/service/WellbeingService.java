package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Wellbeing;
import ru.panfio.telescreen.repository.WellbeingRepository;
import ru.panfio.telescreen.dao.WellbeingDao;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WellbeingService implements Processing {
    @Autowired  //todo delete
    private WellbeingRepository wellbeingRepository;

    private static final int MIN_USAGE_TIME = 5000;
    private static final int MAX_SIZE = 500;

    private final MessageBus messageBus;
    private final WellbeingDao wellbeingDao;

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param wellbeingDao wellbeingDao
     */
    public WellbeingService(WellbeingDao wellbeingDao,
                            MessageBus messageBus) {
        this.messageBus = messageBus;
        this.wellbeingDao = wellbeingDao;
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
                messageBus.send("wellbeing", activity);
                tmp.remove(id);
            }
        }
        log.info("End processing Wellbeing history");
    }

    @Override
    public void process() {
        processWellbeingRecords();
    }
}
