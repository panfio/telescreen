package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.Call;
import ru.panfio.telescreen.service.CallService;

import java.time.Instant;

@CrossOrigin
@RestController
@RequestMapping("/api/call")
public class CallController {

    private final CallService callService;

    /**
     * Constructor.
     *
     * @param callService service
     */
    public CallController(CallService callService) {
        this.callService = callService;
    }

    /**
     * Get Call history by period.
     *
     * @param from time
     * @param to   time
     * @return call records
     */
    @ApiOperation(value = "Get Call history by period")
    @GetMapping
    public ResponseEntity<Iterable<Call>> findCallRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        return new ResponseEntity<>(
                callService.getCallHistoryBetweenDates(
                        Instant.parse(from),
                        Instant.parse(to)
                ),
                HttpStatus.OK);
    }

    /**
     * Processing Call History records.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing Call History records")
    @GetMapping("/process")
    public ResponseEntity processCallHistory() {
        callService.processCallHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
