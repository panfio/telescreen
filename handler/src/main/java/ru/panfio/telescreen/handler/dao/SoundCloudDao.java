package ru.panfio.telescreen.handler.dao;

import ru.panfio.telescreen.handler.model.soundcloud.PlayHistory;
import ru.panfio.telescreen.handler.model.soundcloud.TrackInfo;

import java.util.List;
import java.util.Map;

public interface SoundCloudDao {
    /**
     * Collects sounds info.
     *
     * @return tracks info
     */
    Map<String, TrackInfo> tracksInfo();
    /**
     * Return SoundCloud recently played history.
     *
     * @return recently played sounds list
     */
    List<PlayHistory> recentlyPlayed();
}
