package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.dao.MiFitDao;
import ru.panfio.telescreen.handler.model.MiFitActivity;

import java.util.List;

@Slf4j
@Service
public class MiFitService implements Processing {

    @Autowired
    private MiFitDao miFitDao;

    @Autowired
    private MessageBus messageBus;

    @Override
    public void process() {
        log.info("Start processing Mi Fit daily activity history");
        List<MiFitActivity> activities = miFitDao.getActivities();
        activities.forEach(this::sendActivity);
        log.info("End processing Mi Fit daily activity history");
    }

    private void sendActivity(MiFitActivity activity) {
        messageBus.send("mifit", activity);
    }

    @Override
    public String name() {
        return "mifit";
    }
}
