package ru.panfio.telescreen.handler.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.Wellbeing;
import ru.panfio.telescreen.handler.util.CustomSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class WellbeingDaoJdbc implements WellbeingDao {
    private final DbManager dbManager;


    public WellbeingDaoJdbc(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public List<Wellbeing> getActivities() {
        JdbcTemplate appHistory = dbManager.getTemplate("wellbeing/app_usage");
        return appHistory.query(
                CustomSQL.APP_ACTIVITY_SQL,
                new WellbeingActivityRowMapper());
    }

    private static class WellbeingActivityRowMapper implements RowMapper<Wellbeing> {
        @Override
        public Wellbeing mapRow(ResultSet rs, int i) throws SQLException {
            return Wellbeing.builder()
                    .id(rs.getLong("instance_id"))
                    .type(rs.getInt("type"))
                    .startTime(rs.getTimestamp("timestamp").toInstant())
                    .endTime(rs.getTimestamp("timestamp").toInstant())
                    .app(rs.getString("package_name"))
                    .build();
        }
    }
}
