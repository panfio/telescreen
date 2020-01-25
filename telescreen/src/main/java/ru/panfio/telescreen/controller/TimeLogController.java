package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.TimeLog;
import ru.panfio.telescreen.service.TimeLogService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@CrossOrigin
@RestController
@RequestMapping("/api/timelog")
public class TimeLogController {

    private final TimeLogService timeLogService;

    /**
     * Constructor.
     *
     * @param timeLogService service
     */
    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    /**
     * Get TimeLog records by period.
     *
     * @param from time
     * @param to   time
     * @return timelog records
     */
    @ApiOperation(value = "Get TimeLog records by period")
    @GetMapping
    public ResponseEntity<Iterable<TimeLog>> findTimeLogByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.ofInstant(
                Instant.parse(from), ZoneOffset.systemDefault());
        LocalDateTime t2 = LocalDateTime.ofInstant(
                Instant.parse(to), ZoneOffset.systemDefault());
        return new ResponseEntity<>(
                timeLogService.getTimeLogRecordsBetweenDates(t1, t2),
                HttpStatus.OK);
    }

    /**
     * Processing Timesheet records.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing Timesheet records")
    @GetMapping("/process/timesheet")
    public ResponseEntity processTimesheetRecords() {
        if (timeLogService.processTimesheetRecords()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
