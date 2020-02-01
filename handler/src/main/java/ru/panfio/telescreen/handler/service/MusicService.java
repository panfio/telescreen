package ru.panfio.telescreen.handler.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.dao.SoundCloudDao;
import ru.panfio.telescreen.handler.model.Music;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MusicService implements Processing {
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

        for (var track : listenedTracks) {
            final String key = track.getExternalId();
            if (!soundsInfo.containsKey(key)) {
                log.error("Track info not found. "
                        + "Please refresh listening history in the App");
                continue;
            }
            var activity = (Music) soundsInfo.get(key).clone();
            activity.setListenTime(track.getListenTime());
            activity.setId(track.getId());
            messageBus.send("music", activity);
        }
        log.info("End processing SoundCloud history");
    }

    @Override
    public void process() {
        processSoundCloud();
    }

    @Override
    public String name() {
        return "music";
    }
}
