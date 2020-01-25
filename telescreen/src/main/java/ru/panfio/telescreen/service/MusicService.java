package ru.panfio.telescreen.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Music;
import ru.panfio.telescreen.repository.MusicRecordRepository;
import ru.panfio.telescreen.dao.SoundCloudDao;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class MusicService implements Processing {

    private final MusicRecordRepository musicRecordRepository;
    private final SoundCloudDao soundCloudDao;

    /**
     * Constructor.
     *
     * @param musicRecordRepository repo
     * @param soundCloudDao         dbManager
     */
    public MusicService(MusicRecordRepository musicRecordRepository,
                        SoundCloudDao soundCloudDao) {
        this.musicRecordRepository = musicRecordRepository;
        this.soundCloudDao = soundCloudDao;
    }

    /**
     * Processing the SoundCloud listening history.
     */
    @SneakyThrows
    public void processSoundCloud() {
        log.info("Start processing SoundCloud history");
        List<Music> listenedTracks = soundCloudDao.recentlyPlayed();
        Map<String, Music> soundsInfo = soundCloudDao.soundsInfo();

        List<Music> musicList = new ArrayList<>();
        for (Music track : listenedTracks) {
            final String key = track.getExternalId();
            if (!soundsInfo.containsKey(key)) {
                log.error("Track info not found. "
                        + "Please refresh listening history in the App");
                continue;
            }
            Music info = (Music) soundsInfo.get(key).clone();
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
        musicRecordRepository.saveAll(records);
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
        return musicRecordRepository.findByListenTimeBetween(from, to);
    }

    @Override
    public void process() {
        processSoundCloud();
    }
}
