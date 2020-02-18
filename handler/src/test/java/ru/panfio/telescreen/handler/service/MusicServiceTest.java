package ru.panfio.telescreen.handler.service;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.handler.dao.SoundCloudDao;
import ru.panfio.telescreen.handler.model.Music;
import ru.panfio.telescreen.handler.model.soundcloud.PlayHistory;
import ru.panfio.telescreen.handler.model.soundcloud.TrackInfo;
import ru.panfio.telescreen.handler.util.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MusicServiceTest {

    public SoundCloudDao soundCloudDao = new SoundCloudDao() {
        @Override
        public Map<String, TrackInfo> tracksInfo() {
            Map<String, TrackInfo> expectedTrackInfos = new HashMap<>();
            String[] trackInfos = {
                    "{\"id\":\"732251920\",\"artist\":\"Vesky\",\"title\":\"Leaving\",\"url\":\"https://soundcloud.com/vskymusic/leaving\"}",
                    "{\"id\":\"745949599\",\"artist\":\"-Bucky-\",\"title\":\"Bucky - Night Racer\",\"url\":\"https://soundcloud.com/bucky-music/bucky-night-racer\"}",
                    "{\"id\":\"746114746\",\"artist\":\"vibe.digital\",\"title\":\"Episode 062 - A Look Forward at 2020\",\"url\":\"https://soundcloud.com/vibe-digital/episode062\"}"
            };
            for (String json : trackInfos) {
                final TrackInfo info = Json.parse(json, TrackInfo.class);
                expectedTrackInfos.put(info.getId(), info);
            }
            return expectedTrackInfos;
        }

        @Override
        public List<PlayHistory> recentlyPlayed() {
            List<PlayHistory> expectedPlayHistoryList = new ArrayList<>();
            String[] playHistory = {
                    "{\"id\":1579856369307,\"externalId\":\"732251920\",\"listenTime\":\"2020-01-24T08:59:29.307Z\"}",
                    "{\"id\":1579856079704,\"externalId\":\"745949599\",\"listenTime\":\"2020-01-24T08:54:39.704Z\"}",
                    "{\"id\":1579855591640,\"externalId\":\"746114746\",\"listenTime\":\"2020-01-24T08:46:31.640Z\"}"
            };
            for (String json : playHistory) {
                expectedPlayHistoryList.add(Json.parse(json, PlayHistory.class));
            }
            return expectedPlayHistoryList;
        }
    };
    private SoundCloudMusicService service;
    private MessageBus messageBus;
    private List<Music> expectedMusicList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        messageBus = mock(MessageBus.class);
        service = new SoundCloudMusicService();//soundCloudDao, messageBus);
        String[] musics = {
                "{\"id\":1579856369307,\"externalId\":\"732251920\",\"type\":\"SOUNDCLOUD\",\"artist\":\"Vesky\",\"title\":\"Leaving\",\"listenTime\":\"2020-01-24T08:59:29.307Z\",\"url\":\"https://soundcloud.com/vskymusic/leaving\"}",
                "{\"id\":1579856079704,\"externalId\":\"745949599\",\"type\":\"SOUNDCLOUD\",\"artist\":\"-Bucky-\",\"title\":\"Bucky - Night Racer\",\"listenTime\":\"2020-01-24T08:54:39.704Z\",\"url\":\"https://soundcloud.com/bucky-music/bucky-night-racer\"}",
                "{\"id\":1579855591640,\"externalId\":\"746114746\",\"type\":\"SOUNDCLOUD\",\"artist\":\"vibe.digital\",\"title\":\"Episode 062 - A Look Forward at 2020\",\"listenTime\":\"2020-01-24T08:46:31.640Z\",\"url\":\"https://soundcloud.com/vibe-digital/episode062\"}"
        };
        for (String json : musics) {
            expectedMusicList.add(Json.parse(json, Music.class));
        }
    }

    @Test
    void processSoundCloud() {
        service.process();

        @SuppressWarnings("unchecked") final ArgumentCaptor<List<Music>> argument = ArgumentCaptor.forClass(List.class);
        verify(messageBus).sendAll(anyString(), argument.capture());
        List<Music> list = argument.getValue();

        assertEquals(3, list.size());
        //ideally, this should be sorted and compared using deep equals
        assertEquals(expectedMusicList.toString(), list.toString());
    }

    @Test
    void name() {
        assertEquals("music", service.name());
    }

}