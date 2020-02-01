package ru.panfio.telescreen.handler.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.Music;
import ru.panfio.telescreen.handler.util.CustomSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SoundCloudDaoJdbc implements SoundCloudDao {
    private final DbManager dbManager;

    /**
     * Constructor.
     *
     * @param dbManager dbManager
     */
    public SoundCloudDaoJdbc(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Collects sounds info.
     *
     * @return info map
     */
    public Map<String, Music> soundsInfo() {
        JdbcTemplate soundsInfo = dbManager.getTemplate(
                "soundcloud/SoundCloud");
        Map<String, Music> infos = new HashMap<>();
        soundsInfo.query(CustomSQL.SOUND_INFO_SQL, new MusicInfoMapper())
                .forEach(track -> {
                    infos.put(track.getExternalId(), track);
                });
        return infos;
    }

    /**
     * Return SoundCloud recently played history.
     *
     * @return recently played sounds list
     */
    public List<Music> recentlyPlayed() {
        JdbcTemplate recentlyPlayed = dbManager.getTemplate(
                "soundcloud/collection.db");
        return recentlyPlayed.query(
                CustomSQL.PLAY_HISTORY_SQL, new RecentlyPlayedMapper());
    }

    private static class MusicInfoMapper implements RowMapper<Music> {
        @Override
        public Music mapRow(ResultSet rs, int i) throws SQLException {
            var record = new Music();
            record.setExternalId(rs.getString("id"));
            record.setArtist(rs.getString("username"));
            record.setTitle(rs.getString("title"));
            record.setUrl(rs.getString("permalink_url"));
            record.setType(Music.Type.SOUNDCLOUD);
            return record;
        }
    }

    private static class RecentlyPlayedMapper implements RowMapper<Music> {
        @Override
        public Music mapRow(ResultSet rs, int i) throws SQLException {
            var record = new Music();
            record.setId(rs.getLong("timestamp"));
            record.setExternalId(rs.getString("track_id"));
            record.setListenTime(
                    rs.getTimestamp("timestamp").toInstant());
            return record;
        }
    }
}
