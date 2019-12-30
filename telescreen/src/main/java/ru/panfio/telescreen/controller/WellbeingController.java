package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.model.Wellbeing;
import ru.panfio.telescreen.service.WellbeingService;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WellbeingController {

    private final WellbeingService wellbeingService;

    /**
     * Constructor.
     *
     * @param wellbeingService service
     */
    public WellbeingController(WellbeingService wellbeingService) {
        this.wellbeingService = wellbeingService;
    }

    /**
     * Processing Wellbeing records.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing Wellbeing records")
    @GetMapping("/wellbeing/process")
    public ResponseEntity processWellbeingRecords() {
        wellbeingService.processWellbeingRecords();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get Wellbeing history by period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    @ApiOperation(value = "Get Wellbeing history by period")
    @GetMapping("/wellbeing")
    ResponseEntity<List<Wellbeing>> findWellbeingByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity<List<Wellbeing>>(
                wellbeingService.getWellbeingBetweenDates(t1, t2),
                HttpStatus.OK);
    }
}
