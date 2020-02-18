package ru.panfio.telescreen.handler.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.soundcloud.PlayHistory;
import ru.panfio.telescreen.handler.model.soundcloud.TrackInfo;
import ru.panfio.telescreen.handler.util.CustomSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SoundCloudDaoJdbc implements SoundCloudDao {
    private final DbManager dbManager;

    public SoundCloudDaoJdbc(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Collects sounds info.
     *
     * @return info map
     */
    public Map<String, TrackInfo> tracksInfo() {
        JdbcTemplate soundsInfo = dbManager.getTemplate(
                "soundcloud/SoundCloud");
        Map<String, TrackInfo> infos = new HashMap<>();
        soundsInfo.query(CustomSQL.SOUND_INFO_SQL, new TrackInfoMapper())
                .forEach(track -> infos.put(track.getId(), track));
        return infos;
    }

    /**
     * Return SoundCloud recently played history.
     *
     * @return recently played sounds list
     */
    public List<PlayHistory> recentlyPlayed() {
        JdbcTemplate recentlyPlayed = dbManager.getTemplate(
                "soundcloud/collection.db");
        return recentlyPlayed.query(
                CustomSQL.PLAY_HISTORY_SQL, new PlayHistoryMapper());
    }

    private static class TrackInfoMapper implements RowMapper<TrackInfo> {
        @Override
        public TrackInfo mapRow(ResultSet rs, int i) throws SQLException {
            return TrackInfo.builder()
                    .id(rs.getString("id"))
                    .artist(rs.getString("username"))
                    .title(rs.getString("title"))
                    .url(rs.getString("permalink_url"))
                    .build();
        }
    }

    private static class PlayHistoryMapper implements RowMapper<PlayHistory> {
        @Override
        public PlayHistory mapRow(ResultSet rs, int i) throws SQLException {
            return PlayHistory.builder()
                    .id(rs.getLong("timestamp"))
                    .externalId(rs.getString("track_id"))
                    .listenTime(rs.getTimestamp("timestamp").toInstant())
                    .build();
        }
    }
}
