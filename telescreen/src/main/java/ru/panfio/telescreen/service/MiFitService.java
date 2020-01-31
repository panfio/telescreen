package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.MiFitActivity;
import ru.panfio.telescreen.repository.MiFitActivityRepository;
import ru.panfio.telescreen.dao.MiFitDao;
import ru.panfio.telescreen.util.Json;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class MiFitService implements Processing {
    @Autowired //todo remove
    private MiFitActivityRepository miFitActivityRepo;
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
        for (MiFitActivity activity : activities) {
            messageBus.send("mifit", Json.toJson(activity));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process() {
        processMiFitActivity();
    }

    /**
     * Finds and returns the mi fit activity records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public List<MiFitActivity> getMiFitActivityBetweenDates(Instant from,
                                                            Instant to) {
        return miFitActivityRepo.findByDateBetween(from, to);
    }
}
