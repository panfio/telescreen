package ru.panfio.telescreen.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Music;
import ru.panfio.telescreen.repository.MusicRecordRepository;
import ru.panfio.telescreen.dao.SoundCloudDao;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class MusicService implements Processing {
    @Autowired  //todo remove
    private MusicRecordRepository musicRecordRepository;
    private final MessageBus messageBus;
    private final SoundCloudDao soundCloudDao;

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param soundCloudDao soundCloudDao
     */
    public MusicService(SoundCloudDao soundCloudDao,
                        MessageBus messageBus) {
        this.messageBus = messageBus;
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

        for (Music track : listenedTracks) {
            final String key = track.getExternalId();
            if (!soundsInfo.containsKey(key)) {
                log.error("Track info not found. "
                        + "Please refresh listening history in the App");
                continue;
            }
            Music activity = (Music) soundsInfo.get(key).clone();
            activity.setListenTime(track.getListenTime());
            activity.setId(track.getId());
            messageBus.send("music", activity);
        }
        log.info("End processing SoundCloud history");
    }

    /**
     * Finds and returns the listened music records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<Music> getListenRecordsBetweenDates(
            Instant from, Instant to) {
        return musicRecordRepository.findByListenTimeBetween(from, to);
    }

    @Override
    public void process() {
        processSoundCloud();
    }
}
