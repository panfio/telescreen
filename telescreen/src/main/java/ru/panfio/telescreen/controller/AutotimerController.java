package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.panfio.telescreen.repository.AutotimerRepository;
import ru.panfio.telescreen.service.AutotimerService;

import java.time.LocalDateTime;

@CrossOrigin
@Controller
public class AutotimerController {

    private static final Logger logger = LoggerFactory.getLogger(AutotimerController.class);

    @Autowired
    AutotimerService as;

    @Deprecated
    @ApiOperation(value = "Get all autotimer records")
    @RequestMapping(value = "/time/all", method = RequestMethod.GET)
    ResponseEntity findAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity(as.findAll(), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Get autotimer records by period")
    @RequestMapping(value = "/autotimer", method = RequestMethod.GET)
    ResponseEntity findAutotimerRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity(as.getAllBetweenDates(t1, t2), HttpStatus.OK);
    }
}
