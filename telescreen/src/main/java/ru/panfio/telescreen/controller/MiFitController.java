package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.MiFitActivity;
import ru.panfio.telescreen.service.MiFitService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/mifit")
public class MiFitController {

    private final MiFitService miFitService;

    /**
     * Constructor.
     *
     * @param miFitService service
     */
    public MiFitController(MiFitService miFitService) {
        this.miFitService = miFitService;
    }

    /**
     * Processing Mi Fit activity records.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing Mi Fit activity records")
    @GetMapping("/process")
    public ResponseEntity processMiFitActivities() {
        miFitService.processMiFitActivity();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get Mi Fit activity history by period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    @ApiOperation(value = "Get Mi Fit activity history by period")
    @GetMapping
    public ResponseEntity<List<MiFitActivity>> findMiFitActivityByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.ofInstant(
                Instant.parse(from), ZoneOffset.systemDefault());
        LocalDateTime t2 = LocalDateTime.ofInstant(
                Instant.parse(to), ZoneOffset.systemDefault());
        return new ResponseEntity<List<MiFitActivity>>(
                miFitService.getMiFitActivityBetweenDates(t1, t2),
                HttpStatus.OK);
    }
}
