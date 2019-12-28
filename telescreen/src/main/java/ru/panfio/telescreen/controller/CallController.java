package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.CallRecord;
import ru.panfio.telescreen.service.CallService;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("/api")
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
    @GetMapping("/call")
    ResponseEntity<Iterable<CallRecord>> findCallRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity<>(
                callService.getCallHistoryBetweenDates(t1, t2),
                HttpStatus.OK);
    }

    /**
     * Processing Call History records.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing Call History records")
    @GetMapping("/call/process")
    public ResponseEntity processCallHistory() {
        callService.processCallHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
