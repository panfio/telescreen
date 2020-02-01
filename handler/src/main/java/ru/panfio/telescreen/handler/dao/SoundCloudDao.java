package ru.panfio.telescreen.handler.dao;

import ru.panfio.telescreen.handler.model.Music;

import java.util.List;
import java.util.Map;

public interface SoundCloudDao {
    /**
     * Collects sounds info.
     *
     * @return info map
     */
    Map<String, Music> soundsInfo();
    /**
     * Return SoundCloud recently played history.
     *
     * @return recently played sounds list
     */
    List<Music> recentlyPlayed();
}
