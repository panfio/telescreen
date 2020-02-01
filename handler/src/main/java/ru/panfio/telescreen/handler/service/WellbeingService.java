package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.dao.WellbeingDao;
import ru.panfio.telescreen.handler.model.Wellbeing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WellbeingService implements Processing {
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
     * Processing App activity from Wellbeing database.
     */
    public void processWellbeingRecords() {
        log.info("Start processing Wellbeing history");
        List<Wellbeing> activities = wellbeingDao.getActivities();

        Map<Long, Wellbeing> tmp = new HashMap<>();
        for (var activity : activities) {
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

    @Override
    public String name() {
        return "wellbeing";
    }
}
