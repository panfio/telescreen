package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.TimeLog;
import ru.panfio.telescreen.service.TimeLogService;

import java.time.Instant;

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
        return new ResponseEntity<>(
                timeLogService.getTimeLogRecordsBetweenDates(
                        Instant.parse(from),
                        Instant.parse(to)
                ),
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
