package ru.panfio.telescreen.handler.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.handler.service.CommonService;

@CrossOrigin
@RestController
@RequestMapping
@Api(tags = "processing")
public class ProcessController {
    private final CommonService commonService;

    public ProcessController(CommonService commonService) {
        this.commonService = commonService;
    }

    @ApiOperation(value = "Processing all files")
    @GetMapping("/process/all")
    public ResponseEntity processAll() {
        commonService.processAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Processing specific data")
    @GetMapping("/process/{service}")
    public ResponseEntity process(@PathVariable(value = "service") String service) {
        commonService.process(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

