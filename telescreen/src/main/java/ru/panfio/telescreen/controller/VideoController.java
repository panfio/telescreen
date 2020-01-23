package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.YouTube;
import ru.panfio.telescreen.service.VideoService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class VideoController {

    private final VideoService videoService;

    /**
     * Constructor.
     *
     * @param videoService service.
     */
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * Get YouTube history records by period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    @ApiOperation(value = "Get YouTube records by period")
    @GetMapping("/video/youtube")
    public ResponseEntity<Iterable<YouTube>> findYouTubeByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.ofInstant(
                Instant.parse(from), ZoneOffset.systemDefault());
        LocalDateTime t2 = LocalDateTime.ofInstant(
                Instant.parse(to), ZoneOffset.systemDefault());
        return new ResponseEntity<>(
                videoService.getYouTubeRecordsBetweenDates(t1, t2),
                HttpStatus.OK);
    }

    /**
     * Processing YouTube from google export.
     * @return ok
     */
    @ApiOperation(value = "Processing YouTube from google export")
    @GetMapping("/video/process/youtube")
    public ResponseEntity processYouTubeRecords() {
        videoService.processYouTubeHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
