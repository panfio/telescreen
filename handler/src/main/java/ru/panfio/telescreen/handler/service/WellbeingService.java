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
    private final MessageBus messageBus;
    private final WellbeingDao wellbeingDao;

    /**
     * Constructor.
     *
     * @param messageBus   message bus
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

        Map<Long, Wellbeing> startedActivities = new HashMap<>();
        for (var activity : activities) {
            final var id = activity.getId();
            final var type = activity.getType();

            if (type == 1) { //activity started
                startedActivities.put(id, activity);
            }

            if (type == 2) { //activity ended
                var started = startedActivities.get(id);
                if (started == null) {
                    continue;
                }
                //Android activity has a unique ID but
                //there are many activities with the same ID
                activity.setId(null);

                activity.setStartTime(started.getStartTime());
                messageBus.send("wellbeing", activity);
                startedActivities.remove(id);
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
