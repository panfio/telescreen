package ru.panfio.telescreen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.panfio.telescreen.service.CommonService;

@RestController
@Api(tags = "common")
@RequestMapping("/api")
public class CommonController {

    private final CommonService service;

    /**
     * Constructor.
     *
     * @param service service
     */
    public CommonController(CommonService service) {
        this.service = service;
    }

    /**
     * Processing all files.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing all files")
    @GetMapping("/process")
    public ResponseEntity processAll() {
        service.processAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
