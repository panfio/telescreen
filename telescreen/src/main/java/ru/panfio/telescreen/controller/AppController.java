package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.service.AppService;

import java.time.LocalDateTime;

@CrossOrigin
@Controller
public class AppController {

    @Autowired
    AppService app;

    @ApiOperation(value = "Get YouTube records by period")
    @RequestMapping(value = "/app/youtube", method = RequestMethod.GET)
    ResponseEntity findYouTubeByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity(app.getYouTubeRecordsBetweenDates(t1, t2), HttpStatus.OK);
    }

    @ApiOperation(value = "Get TimeLog records by period")
    @RequestMapping(value = "/app/timelog", method = RequestMethod.GET)
    ResponseEntity findTimeLogByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity(app.getTimeLogRecordsBetweenDates(t1, t2), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Listen history by period")
    @RequestMapping(value = "/app/listenhistory", method = RequestMethod.GET)
    ResponseEntity findListenRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity(app.getListenRecordsBetweenDates(t1, t2), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Wellbeing history by period")
    @RequestMapping(value = "/app/wellbeing", method = RequestMethod.GET)
    ResponseEntity findWellbeingByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity(app.getWellbeingBetweenDates(t1, t2), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Call history by period")
    @RequestMapping(value = "/app/call", method = RequestMethod.GET)
    ResponseEntity findCallRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity(app.getCallHistoryBetweenDates(t1, t2), HttpStatus.OK);
    }

    @ApiOperation(value = "Get autotimer records by period")
    @RequestMapping(value = "/app/autotimer", method = RequestMethod.GET)
    ResponseEntity findAutotimerRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity(app.getAutotimerRecordsBetweenDates(t1, t2), HttpStatus.OK);
    }

    @ApiOperation(value = "Get massage history by period")
    @RequestMapping(value = "/app/message", method = RequestMethod.GET)
    ResponseEntity findMessageHistoryByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity(app.getMessageHistoryBetweenDates(t1, t2), HttpStatus.OK);
    }
}
