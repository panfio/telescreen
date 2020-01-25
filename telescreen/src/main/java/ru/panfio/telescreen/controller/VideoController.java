package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.YouTube;
import ru.panfio.telescreen.service.VideoService;

import java.time.Instant;

@CrossOrigin
@RestController
@RequestMapping("/api/video")
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
    @GetMapping("/youtube")
    public ResponseEntity<Iterable<YouTube>> findYouTubeByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        return new ResponseEntity<>(
                videoService.getYouTubeRecordsBetweenDates(
                        Instant.parse(from),
                        Instant.parse(to)
                ),
                HttpStatus.OK);
    }

    /**
     * Processing YouTube from google export.
     * @return ok
     */
    @ApiOperation(value = "Processing YouTube from google export")
    @GetMapping("/process/youtube")
    public ResponseEntity processYouTubeRecords() {
        videoService.processYouTubeHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
