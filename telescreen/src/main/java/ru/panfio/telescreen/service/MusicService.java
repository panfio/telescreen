package ru.panfio.telescreen.service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Music;
import ru.panfio.telescreen.model.spotify.RecentlyPlayedProto;
import ru.panfio.telescreen.repository.ListenRecordRepository;
import ru.panfio.telescreen.service.util.DbManager;
import ru.panfio.telescreen.util.CustomSQL;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class MusicService implements Processing {

    //TODO rename
    private final ListenRecordRepository listenRecordRepository;

    private final ObjectStorage objectStorage;

    private final DbManager dbManager;

    /**
     * Constructor.
     *
     * @param listenRecordRepository repo
     * @param objectStorage          service
     * @param dbManager              dbManager
     */
    public MusicService(ListenRecordRepository listenRecordRepository,
                        ObjectStorage objectStorage,
                        DbManager dbManager) {
        this.listenRecordRepository = listenRecordRepository;
        this.objectStorage = objectStorage;
        this.dbManager = dbManager;
    }

    /**
     * Processing Spotify listen history.
     *
     * @param accessToken token from
     *                    https://developer.spotify.com/console/get-track/
     */
    public void processSpotifyRecentlyPlayed(String accessToken) {
        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        //todo process list of files
        String filename = "spotify/recently_played.bnk";
        InputStream stream = objectStorage.getInputStream(filename);
        try {
            RecentlyPlayedProto.RecentlyPlayed rp =
                    RecentlyPlayedProto.RecentlyPlayed.parseFrom(stream);
//            int i = 0;
            List<Music> listenedTracks = new ArrayList<>();
            for (RecentlyPlayedProto.RecentTrack item : rp.getTList()) {
                String trackId = item.getTrack().substring(
                        item.getTrack().lastIndexOf(":") + 1);

                Music lr = new Music();
                lr.setExternalId(trackId);
                lr.setType(Music.Type.SPOTIFY);
                lr.setId((long) item.getTimestamp());
                lr.setArtist(trackId);
                lr.setTitle(trackId);
                lr.setListenTime(LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(item.getTimestamp()),
                        TimeZone.getDefault().toZoneId()));
                final GetTrackRequest getTrackRequest =
                        spotifyApi.getTrack(trackId).build();
                try {
                    Track track = getTrackRequest.execute();

                    lr.setArtist(track.getArtists()[0].getName());
                    lr.setTitle(track.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SpotifyWebApiException e) {
                    e.printStackTrace();
                }
                listenedTracks.add(lr);
//                i++; if (i > 10) return;
            }
            saveListenRecords(listenedTracks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processing the SoundCloud listening history.
     */
    public void processSoundCloud() {
        log.info("Start processing SoundCloud history");
        JdbcTemplate recentlyPlayed = dbManager.getTemplate(
                "soundcloud/collection.db");
        JdbcTemplate soundsInfo = dbManager.getTemplate(
                "soundcloud/SoundCloud");
        List<Music> listenedTracks = recentlyPlayed.query(
                CustomSQL.PLAY_HISTORY_SQL, new RecentlyPlayedMapper());

        List<Music> musicList = new ArrayList<>();
        for (Music track : listenedTracks) {
            Music info = soundsInfo.queryForObject(
                    CustomSQL.SOUND_INFO_SQL,
                    new Object[]{track.getExternalId()},
                    new MusicInfoMapper());
            if (info == null) {
                log.error("Track info not found. "
                        + "Please refresh listening history in the App");
                continue;
            }
            info.setListenTime(track.getListenTime());
            info.setId(track.getId());
            musicList.add(info);
        }
        saveListenRecords(musicList);
        log.info("End processing SoundCloud history");
    }

    /**
     * Saves listened records in the database.
     *
     * @param records list of records
     */
    public void saveListenRecords(List<Music> records) {
        listenRecordRepository.saveAll(records);
    }

    /**
     * Finds and returns the listened music records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<Music> getListenRecordsBetweenDates(
            LocalDateTime from, LocalDateTime to) {
        return listenRecordRepository.findByListenTimeBetween(from, to);
    }

    @Override
    public void process() {
        processSoundCloud();
    }
}

class RecentlyPlayedMapper implements RowMapper<Music> {

    @Override
    public Music mapRow(ResultSet rs, int i) throws SQLException {
        Music record = new Music();
        record.setId(rs.getLong("timestamp"));
        record.setExternalId(rs.getString("track_id"));
        record.setListenTime(rs.getTimestamp("timestamp").toLocalDateTime());
        return record;
    }
}

class MusicInfoMapper implements RowMapper<Music> {

    @Override
    public Music mapRow(ResultSet rs, int i) throws SQLException {
        Music record = new Music();
        record.setExternalId(rs.getString("id"));
        record.setArtist(rs.getString("username"));
        record.setTitle(rs.getString("title"));
        record.setUrl(rs.getString("permalink_url"));
        record.setType(Music.Type.SOUNDCLOUD);
        return record;
    }
}
