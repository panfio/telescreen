package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.dao.MiFitDao;
import ru.panfio.telescreen.handler.model.MiFitActivity;

import java.util.List;

@Slf4j
@Service
public class MiFitService implements Processing {
    private final MessageBus messageBus;
    private final MiFitDao miFitDao;

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param miFitDao miFitDao
     */
    public MiFitService(MiFitDao miFitDao,
                        MessageBus messageBus) {
        this.messageBus = messageBus;
        this.miFitDao = miFitDao;
    }

    /**
     * Processing Mi Fit activities history.
     */
    public void processMiFitActivity() {
        log.info("Start processing Mi Fit daily activity history");
        List<MiFitActivity> activities = miFitDao.getActivities();
        activities.forEach(this::sendActivity);
    }

    private void sendActivity(MiFitActivity activity) {
        messageBus.send("mifit", activity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process() {
        processMiFitActivity();
    }

    @Override
    public String name() {
        return "mifit";
    }
}
