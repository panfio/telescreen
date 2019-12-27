package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.panfio.telescreen.service.ProcessService;

import java.util.Collections;


@Controller
public class ProcessController {//CHECKSTYLE:OFF

    private final ProcessService ps;

    @Autowired
    public ProcessController(ProcessService ps) {
        this.ps = ps;
    }

    @ApiOperation(value = "Processing all")
    @RequestMapping(value = "/process/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processAll() {
        ps.processAll();
        return new ResponseEntity<>(
                Collections.singletonMap("processed", "all"),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Processing media files")
    @GetMapping("/process/media")
    public ResponseEntity processMediaRecords() {
        if (ps.processMediaRecords()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Processing \"Аutotimer\" records")
    @GetMapping("/process/autotimer")
    public ResponseEntity processAutotimerRecords() {
        if (ps.processAutotimerRecords()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Processing Spotify recently played")
    @RequestMapping(value = "/process/spotify", method = RequestMethod.GET)
    public ResponseEntity processSpotify(
            @RequestParam(value = "accessToken") String accessToken)
            throws Exception {
        ps.processSpotifyRecentlyPlayed(accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Processing SoundCloud history")
    @RequestMapping(value = "/process/soundcloud", method = RequestMethod.GET)
    public ResponseEntity processSoundCloud() throws Exception {
        ps.processSoundCloud();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Processing Timesheet records")
    @RequestMapping(value = "/process/timesheet", method = RequestMethod.GET)
    public ResponseEntity processTimesheetRecords() {
        if (ps.processTimesheetRecords()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Processing YouTube from google export")
    @RequestMapping(value = "/process/youtube", method = RequestMethod.GET)
    public ResponseEntity processYouTubeRecords() {
        ps.processYouTubeHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Processing Wellbeing records")
    @RequestMapping(value = "/process/wellbeing", method = RequestMethod.GET)
    public ResponseEntity processWellbeingRecords() {
        ps.processWellbeingRecords();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Processing Call History records")
    @RequestMapping(value = "/process/call", method = RequestMethod.GET)
    public ResponseEntity processCallHistory() {
        ps.processCallHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Processing Telegram message History")
    @RequestMapping(value = "/process/telegram", method = RequestMethod.GET)
    public ResponseEntity processTelegramHistory() {
        ps.processTelegramHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //CHECKSTYLE:ON
}
