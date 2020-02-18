package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.dao.SoundCloudDao;
import ru.panfio.telescreen.handler.model.Music;
import ru.panfio.telescreen.handler.model.soundcloud.PlayHistory;
import ru.panfio.telescreen.handler.model.soundcloud.TrackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SoundCloudMusicService implements Processing {

    @Autowired
    MessageBus messageBus;

    @Autowired
    SoundCloudDao soundCloudDao;

    @Override
    public void process() {
        log.info("Start processing SoundCloud history");
        Map<String, TrackInfo> trackInfos = getTrackInfos();
        List<PlayHistory> listenedTracks = getListenedTracks();
        List<Music> collectedTracks = collectTracks(trackInfos, listenedTracks);
        sendMessages(collectedTracks);
        log.info("End processing SoundCloud history");
    }

    public List<Music> collectTracks(Map<String, TrackInfo> trackInfos,
                                     List<PlayHistory> listenedTracks) {
        List<Music> collectedTracks = new ArrayList<>();
        for (PlayHistory listenedTrack : listenedTracks) {
            final var key = listenedTrack.getExternalId();
            if (!trackInfos.containsKey(key)) {
                log.info("Track info not found. "
                        + "Please refresh listening history in the App");
                continue;
            }
            final var trackInfo = trackInfos.get(key);
            Music music = constructMusic(listenedTrack, trackInfo);
            collectedTracks.add(music);
        }
        return collectedTracks;
    }

    private Music constructMusic(PlayHistory track, TrackInfo info) {
        return Music.builder()
                .id(track.getId())
                .listenTime(track.getListenTime())
                .externalId(info.getId())
                .artist(info.getArtist())
                .title(info.getTitle())
                .url(info.getUrl())
                .type(Music.Type.SOUNDCLOUD)
                .build();
    }

    private Map<String, TrackInfo> getTrackInfos() {
        return soundCloudDao.tracksInfo();
    }

    private List<PlayHistory> getListenedTracks() {
        return soundCloudDao.recentlyPlayed();
    }

    private void sendMessages(List<Music> musicList) {
        messageBus.sendAll("music", musicList);
    }

    @Override
    public String name() {
        return "music";
    }
}

