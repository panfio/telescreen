package ru.panfio.telescreen.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.MiFitActivity;
import ru.panfio.telescreen.util.CustomSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@Service
public class MiFitDaoJdbc implements MiFitDao {
    private final DbManager dbManager;

    /**
     * Constructor.
     *
     * @param dbManager dbManager
     */
    public MiFitDaoJdbc(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Gets activities from the database.
     *
     * @return activity list
     */
    @Override
    public List<MiFitActivity> getActivities() {
        JdbcTemplate miData = dbManager.getTemplate("mifit/db/origin_db");
        return miData.query(
                CustomSQL.MIBAND_DAILY_SQL,
                new MiFitActivityMapper());
    }

    private static class MiFitActivityMapper
            implements RowMapper<MiFitActivity> {
        @Override
        public MiFitActivity mapRow(ResultSet rs, int i) throws SQLException {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd [HH:mm:ss]")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();
            Instant date = LocalDateTime.parse(
                    rs.getString("Date"), formatter).toInstant(ZoneOffset.UTC);

            MiFitActivity activity = new MiFitActivity();
            activity.setDate(date);
            activity.setSleepStart(
                    rs.getTimestamp("SleepStart").toInstant());
            activity.setSleepEnd(
                    rs.getTimestamp("SleepEND").toInstant());
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
        }
    }
}
