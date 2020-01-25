package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.Music;
import ru.panfio.telescreen.service.MusicService;

import java.time.Instant;

@CrossOrigin
@RestController
@RequestMapping("/api/music")
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
    @GetMapping
    public ResponseEntity<Iterable<Music>> findListenRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        return new ResponseEntity<>(
                musicService.getListenRecordsBetweenDates(
                        Instant.parse(from),
                        Instant.parse(to)
                ),
                HttpStatus.OK);
    }

    /**
     * Processing SoundCloud history.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing SoundCloud history")
    @GetMapping("/process/soundcloud")
    public ResponseEntity processSoundCloud() {
        musicService.processSoundCloud();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
