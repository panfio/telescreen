package ru.panfio.telescreen.handler.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.Call;
import ru.panfio.telescreen.handler.util.CustomSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class CallDaoJdbc implements CallDao {
    private final DbManager dbManager;

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
            var name = rs.getString("name");
            return Call.builder()
                    .date(rs.getTimestamp("date").toInstant())
                    .duration(rs.getInt("duration"))
                    .number(rs.getString("number"))
                    .name(name.equals("") ? "Unknown" : name)
                    .type(rs.getInt("type"))
                    .build();
        }
    }
}
