package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.MiFitActivity;
import ru.panfio.telescreen.repository.MiFitActivityRepository;
import ru.panfio.telescreen.service.util.DbManager;
import ru.panfio.telescreen.util.CustomSQL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@Slf4j
@Service
public class MiFitService implements Processing {

    private final MiFitActivityRepository miFitActivityRepo;

    private final DbManager dbManager;

    /**
     * Constructor.
     *
     * @param miFitActivityRepo repo
     * @param dbManager         dbHelper
     */
    public MiFitService(MiFitActivityRepository miFitActivityRepo,
                        DbManager dbManager) {
        this.miFitActivityRepo = miFitActivityRepo;
        this.dbManager = dbManager;
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

        JdbcTemplate miData = dbManager.getTemplate("mifit/db/origin_db");
        List<MiFitActivity> activities = miData.query(
                CustomSQL.MIBAND_DAILY_SQL,
                (rs, i) -> {
                    //CHECKSTYLE:OFF
                    //todo move to util class
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                            .appendPattern("yyyy-MM-dd [HH:mm:ss]")
                            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                            .toFormatter();
                    LocalDateTime date = LocalDateTime.parse(
                            rs.getString("Date"), formatter);

                    MiFitActivity activity = new MiFitActivity();
                    activity.setDate(date);
                    activity.setSleepStart(
                            rs.getTimestamp("SleepStart").toLocalDateTime());
                    activity.setSleepEnd(
                            rs.getTimestamp("SleepEND").toLocalDateTime());
                    activity.setInBedMin(rs.getInt("InBedMin"));
                    activity.setLightSleepMin(rs.getInt("LightSleepMin"));
                    activity.setDeepSleepMin(rs.getInt("DeepSleepMin"));
                    activity.setAwakeMin(rs.getInt("AwakeMin"));
                    activity.setDailyDistanceMeter(
                            rs.getInt("DailyDistanceMeter"));
                    activity.setDailySteps(rs.getInt("DailySteps"));
                    activity.setDailyBurnCalories(
                            rs.getInt("DailyBurnCalories"));
                    activity.setWalkDistance(rs.getInt("WalkDistance"));
                    activity.setWalkTimeMin(rs.getInt("WalkTimeMin"));
                    activity.setWalkBurnCalories(
                            rs.getInt("WalkBurnCalories"));
                    activity.setRunDistanceMeter(
                            rs.getInt("RunDistanceMeter"));
                    activity.setRunTimeMin(rs.getInt("RunTimeMin"));
                    activity.setRunBurnCalories(rs.getInt("RunBurnCalories"));
                    return activity;
                    //CHECKSTYLE:ON
                });
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
    public List<MiFitActivity> getMiFitActivityBetweenDates(LocalDateTime from,
                                                            LocalDateTime to) {
        return miFitActivityRepo.findByDateBetween(from, to);
    }
}
