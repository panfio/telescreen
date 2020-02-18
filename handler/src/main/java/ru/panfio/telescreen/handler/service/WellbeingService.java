package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.dao.WellbeingDao;
import ru.panfio.telescreen.handler.model.Wellbeing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WellbeingService implements Processing {
    private final MessageBus messageBus;
    private final WellbeingDao wellbeingDao;

    public WellbeingService(WellbeingDao wellbeingDao,
                            MessageBus messageBus) {
        this.messageBus = messageBus;
        this.wellbeingDao = wellbeingDao;
    }

    @Override
    public void process() {
        log.info("Start processing Wellbeing history");
        List<Wellbeing> wellbeingRecords = getWellbeingRecords();
        List<Wellbeing> activities = collectActivities(wellbeingRecords);
        activities.forEach(this::sendMessage);
        log.info("End processing Wellbeing history");
    }

    public List<Wellbeing> collectActivities(List<Wellbeing> wellbeingRecords) {
        List<Wellbeing> activities = new ArrayList<>();
        Map<Long, Wellbeing> startedActivities = new HashMap<>();
        for (var activity : wellbeingRecords) {
            final var id = activity.getId();
            final var type = activity.getType();

            if (type == ActivityType.START.id()) {
                startedActivities.put(id, activity);
            }

            if (type == ActivityType.END.id()) {
                var started = startedActivities.get(id);
                if (started == null) {
                    continue;
                }
                var record = Wellbeing.builder()
                        .id(null)
                        .type(activity.getType()) //WHAT?
                        .startTime(started.getStartTime())
                        .endTime(activity.getEndTime())
                        .app(activity.getApp())
                        .build();
                activities.add(record);
                startedActivities.remove(id);
            }
        }
        return activities;
    }

    private List<Wellbeing> getWellbeingRecords() {
        return wellbeingDao.getActivities();
    }

    private void sendMessage(Wellbeing activity) {
        messageBus.send("wellbeing", activity);
    }

    @Override
    public String name() {
        return "wellbeing";
    }

    enum ActivityType {
        START(1), END(2);

        private int typeId;

        ActivityType(int typeId) {
            this.typeId = typeId;
        }

        public int id() {
            return typeId;
        }
    }
}
