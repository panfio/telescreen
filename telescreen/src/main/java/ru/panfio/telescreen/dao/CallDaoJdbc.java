package ru.panfio.telescreen.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Call;
import ru.panfio.telescreen.util.CustomSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class CallDaoJdbc implements CallDao {
    private final DbManager dbManager;

    /**
     * Constructor.
     *
     * @param dbManager dbManager
     */
    public CallDaoJdbc(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public List<Call> getPhoneCalls() {
        JdbcTemplate callHistory = dbManager.getTemplate("call/calllog.db");
        return callHistory.query(
                CustomSQL.CALL_HISTORY_SQL, new CallRecordMapper());
    }

    private static class CallRecordMapper implements RowMapper<Call> {
        @Override
        public Call mapRow(ResultSet rs, int i) throws SQLException {
            Call cr = new Call();
            cr.setDate(rs.getTimestamp("date").toLocalDateTime());
            cr.setDuration(rs.getInt("duration"));
            cr.setNumber(rs.getString("number"));
            String name = rs.getString("name");
            cr.setName(name.equals("") ? "Unknown" : name);
            cr.setType(rs.getInt("type"));
            return cr;
        }
    }
}
