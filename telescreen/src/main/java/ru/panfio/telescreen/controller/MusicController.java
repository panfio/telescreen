package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.Music;
import ru.panfio.telescreen.service.MusicService;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MusicController {

    private final MusicService musicService;

    /**
     * Constructor.
     *
     * @param musicService service
     */
    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * Get Listen history by period.
     *
     * @param from time
     * @param to   time
     * @return ok
     */
    @ApiOperation(value = "Get Listen history by period")
    @GetMapping("/music")
    ResponseEntity<Iterable<Music>> findListenRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity<>(
                musicService.getListenRecordsBetweenDates(t1, t2),
                HttpStatus.OK);
    }

    /**
     * Processing Spotify recently played.
     *
     * @param accessToken token
     * @return ok
     */
    @ApiOperation(value = "Processing Spotify recently played")
    @GetMapping("/music/process/spotify")
    public ResponseEntity processSpotify(
            @RequestParam(value = "accessToken") String accessToken) {
        musicService.processSpotifyRecentlyPlayed(accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Processing SoundCloud history.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing SoundCloud history")
    @GetMapping("/music/process/soundcloud")
    public ResponseEntity processSoundCloud() {
        musicService.processSoundCloud();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
