package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.MiFitActivity;
import ru.panfio.telescreen.repository.MiFitActivityRepository;
import ru.panfio.telescreen.dao.MiFitDao;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class MiFitService implements Processing {

    private final MiFitActivityRepository miFitActivityRepo;
    private final MiFitDao miFitDao;

    /**
     * Constructor.
     *
     * @param miFitActivityRepo repo
     * @param miFitDao          miFitDao
     */
    public MiFitService(MiFitActivityRepository miFitActivityRepo,
                        MiFitDao miFitDao) {
        this.miFitActivityRepo = miFitActivityRepo;
        this.miFitDao = miFitDao;
    }

    /**
     * Saves message records in the database.
     *
     * @param records list of records
     */
    public void saveMiFitActivityRecords(List<MiFitActivity> records) {
        for (MiFitActivity activity : records) {
            MiFitActivity dbRecord =
                    miFitActivityRepo.findByDate(activity.getDate());
            if (dbRecord == null) {
                miFitActivityRepo.save(activity);
                continue;
            }
            activity.setId(dbRecord.getId());
            miFitActivityRepo.save(activity);
        }
    }

    /**
     * Processing Mi Fit activities history.
     */
    public void processMiFitActivity() {
        log.info("Start processing Mi Fit daily activity history");
        List<MiFitActivity> activities = miFitDao.getActivities();
        saveMiFitActivityRecords(activities);
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
