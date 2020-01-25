package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.Autotimer;
import ru.panfio.telescreen.service.AutoTimerService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@CrossOrigin
@RestController
@RequestMapping("/api/autotimer")
public class AutoTimerController {

    private final AutoTimerService service;

    /**
     * Constructor.
     * @param service service
     */
    public AutoTimerController(AutoTimerService service) {
        this.service = service;
    }

    /**
     * Processing "АutoTimer" records.
     *
     * @return status
     */
    @ApiOperation(value = "Processing \"Аutotimer\" records")
    @GetMapping("/process")
    public ResponseEntity processAutotimerRecords() {
        if (service.processAutotimerRecords()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Returns "АutoTimer" records by period.
     *
     * @param from time
     * @param to   time
     * @return list of records
     */
    @ApiOperation(value = "Get autotimer records by period")
    @GetMapping
    public ResponseEntity<Iterable<Autotimer>> findAutotimerRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.ofInstant(
                Instant.parse(from), ZoneOffset.systemDefault());
        LocalDateTime t2 = LocalDateTime.ofInstant(
                Instant.parse(to), ZoneOffset.systemDefault());
        return new ResponseEntity<>(
                service.getAutotimerRecordsBetweenDates(t1, t2),
                HttpStatus.OK);
    }
}
