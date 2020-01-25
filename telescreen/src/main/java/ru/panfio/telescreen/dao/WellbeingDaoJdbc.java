package ru.panfio.telescreen.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Wellbeing;
import ru.panfio.telescreen.util.CustomSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class WellbeingDaoJdbc implements WellbeingDao {

    private final DbManager dbManager;

    /**
     * Constructor.
     *
     * @param dbManager dbManager
     */
    public WellbeingDaoJdbc(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Gets activities from the database.
     *
     * @return activity list
     */
    @Override
    public List<Wellbeing> getActivities() {
        JdbcTemplate appHistory = dbManager.getTemplate("wellbeing/app_usage");
        return appHistory.query(
                CustomSQL.APP_ACTIVITY_SQL,
                new WellbeingActivityRowMapper());
    }

    private static class WellbeingActivityRowMapper
            implements RowMapper<Wellbeing> {
        @Override
        public Wellbeing mapRow(ResultSet rs, int i) throws SQLException {
            Wellbeing wr = new Wellbeing();
            wr.setId(rs.getLong("instance_id"));
            wr.setEndTime(rs.getTimestamp("timestamp").toInstant());
            wr.setStartTime(rs.getTimestamp("timestamp").toInstant());
            wr.setType(rs.getInt("type"));
            wr.setApp(rs.getString("package_name"));
            return wr;
        }
    }
}
