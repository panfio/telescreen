package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Wellbeing;
import ru.panfio.telescreen.repository.WellbeingRepository;
import ru.panfio.telescreen.service.util.DbManager;
import ru.panfio.telescreen.util.CustomSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
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

    private final DbManager dbManager;

    /**
     * Constructor.
     *
     * @param wellbeingRepository repo
     * @param dbManager           dbManager
     */
    public WellbeingService(WellbeingRepository wellbeingRepository,
                            DbManager dbManager) {
        this.wellbeingRepository = wellbeingRepository;
        this.dbManager = dbManager;
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
            LocalDateTime from, LocalDateTime to) {
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

        JdbcTemplate appHistory = dbManager.getTemplate("wellbeing/app_usage");
        List<Wellbeing> activities = appHistory.query(
                CustomSQL.APP_ACTIVITY_SQL, new ActivitiesMapper());

        List<Wellbeing> wellbeingActivities = new ArrayList<>();
        Map<Long, Wellbeing> tmp = new HashMap<>();
        for (Wellbeing activity : activities) {
            if (activity.getType() == 1) {
                tmp.put(activity.getId(), activity);
            }
            if (activity.getType() == 2) {
                long id = activity.getId();
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

class ActivitiesMapper implements RowMapper<Wellbeing> {

    @Override
    public Wellbeing mapRow(ResultSet rs, int i) throws SQLException {
        Wellbeing wr = new Wellbeing();
        wr.setId(rs.getLong("instance_id"));
        wr.setEndTime(
                rs.getTimestamp("timestamp").toLocalDateTime());
        wr.setStartTime(
                rs.getTimestamp("timestamp").toLocalDateTime());
        wr.setType(rs.getInt("type"));
        wr.setApp(rs.getString("package_name"));
        return wr;
    }
}
