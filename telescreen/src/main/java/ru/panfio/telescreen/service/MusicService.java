package ru.panfio.telescreen.service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.ListenRecord;
import ru.panfio.telescreen.model.spotify.RecentlyPlayedProto;
import ru.panfio.telescreen.repository.ListenRecordRepository;
import ru.panfio.telescreen.util.CustomSQL;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class MusicService {

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
            List<ListenRecord> listenedTracks = new ArrayList<>();
            for (RecentlyPlayedProto.RecentTrack item : rp.getTList()) {
                String trackId = item.getTrack().substring(
                        item.getTrack().lastIndexOf(":") + 1);

                ListenRecord lr = new ListenRecord();
                lr.setExternalId(trackId);
                lr.setType(ListenRecord.Type.SPOTIFY);
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
     * Returns map of tracks from SoundCloud db.
     *
     * @return map of tracks
     */
    private Map<Long, ListenRecord> getSoundsInfo() {
        Map<Long, ListenRecord> soundsInfo = new HashMap<>();
        try (Connection conn = dbManager.connectSQLite("soundcloud/SoundCloud");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(CustomSQL.SOUND_INFO_SQL)) {
            while (rs.next()) {
                ListenRecord soundInfo = new ListenRecord();
                soundInfo.setExternalId(rs.getString("id"));
                soundInfo.setArtist(rs.getString("username"));
                soundInfo.setTitle(rs.getString("title"));
                soundInfo.setUrl(rs.getString("permalink_url"));

                soundsInfo.put(rs.getLong("id"), soundInfo);
            }
        } catch (SQLException | FileNotFoundException e) {
            log.info("Failed processing SoundCloud sounds info");
        }
        return soundsInfo;
    }

    /**
     * Processing the SoundCloud listening history.
     */
    public void processSoundCloud() {
        log.info("Start processing SoundCloud history");
        Map<Long, ListenRecord> soundsInfo = getSoundsInfo();
        List<ListenRecord> listenRecords = new ArrayList<>();
        try (Connection conn =
                     dbManager.connectSQLite("soundcloud/collection.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(CustomSQL.PLAY_HISTORY_SQL)) {
            while (rs.next()) {
                ListenRecord sound = soundsInfo.get(rs.getLong("track_id"));
                if (sound == null) {
                    continue;
                }
                Long lrId = rs.getLong("timestamp");
                ListenRecord lr = new ListenRecord();
                lr.setId(lrId);
                lr.setExternalId(sound.getExternalId());
                lr.setArtist(sound.getArtist());
                lr.setTitle(sound.getTitle());
                lr.setType(ListenRecord.Type.SOUNDCLOUD);
                lr.setListenTime(
                        rs.getTimestamp("timestamp").toLocalDateTime());
                lr.setUrl(sound.getUrl());

                listenRecords.add(lr);
            }
            saveListenRecords(listenRecords);
        } catch (SQLException | FileNotFoundException e) {
            log.info("Failed processing SoundCloud play history");
        }
        log.info("End processing SoundCloud history");
    }

    /**
     * Saves listened records in the database.
     *
     * @param records list of records
     */
    public void saveListenRecords(List<ListenRecord> records) {
        listenRecordRepository.saveAll(records);
    }

    /**
     * Finds and returns the listened music records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<ListenRecord> getListenRecordsBetweenDates(
            LocalDateTime from, LocalDateTime to) {
        return listenRecordRepository.getAllBetweenDates(from, to);
    }
}
