package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Wellbeing;
import ru.panfio.telescreen.repository.WellbeingRepository;
import ru.panfio.telescreen.util.CustomSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WellbeingService {

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
    public Iterable<Wellbeing> getWellbeingBetweenDates(
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
        List<Wellbeing> wellbeingActivities = new ArrayList<>();
        try (Connection conn = dbManager.connectSQLite("wellbeing/app_usage");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(CustomSQL.APP_ACTIVITY_SQL)) {
            Map<Long, Wellbeing> tmp = new HashMap<>();
            while (rs.next()) {
                if (rs.getInt("type") == 1) {
                    long id = rs.getLong("instance_id");

                    Wellbeing wr = new Wellbeing();
                    wr.setStartTime(
                            rs.getTimestamp("timestamp").toLocalDateTime());
                    wr.setId(id);

                    tmp.put(id, wr);
                }
                if (rs.getInt("type") == 2) {
                    long id = rs.getLong("instance_id");
                    Wellbeing tempWellbringRecord = tmp.get(id);
                    if (tempWellbringRecord == null) {
                        continue;
                    }
                    Wellbeing wr = new Wellbeing();
                    wr.setStartTime(tempWellbringRecord.getStartTime());
                    wr.setEndTime(
                            rs.getTimestamp("timestamp").toLocalDateTime());
                    wr.setType(Wellbeing.Type.ACTIVITY);
                    wr.setApp(rs.getString("package_name"));

                    wellbeingActivities.add(wr);
                    tmp.remove(id);
                }
                if (wellbeingActivities.size() > MAX_SIZE) {
                    saveWellbeingRecords(wellbeingActivities);
                    wellbeingActivities.clear();
                }
            }
            saveWellbeingRecords(wellbeingActivities);
        } catch (Exception e) {
            log.info("Failed processing Wellbeing history");
        }
        log.info("End processing Wellbeing history");
    }

}
