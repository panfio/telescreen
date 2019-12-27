package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.service.MediaService;

import java.time.LocalDateTime;

@CrossOrigin
@Controller
public class MediaController {//CHECKSTYLE:OFF

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Deprecated
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all media records")
    @RequestMapping(value = "/media/all", method = RequestMethod.GET)
    ResponseEntity findAll() {
        return new ResponseEntity(
                mediaService.getAllMediaRecords(),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Get media records by period")
    @RequestMapping(value = "/media", method = RequestMethod.GET)
    ResponseEntity findAutotimerRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity(
                mediaService.getMediaRecordsByPeriod(t1, t2),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/media/file", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFileByFileName(
            @RequestParam(value = "filename") String filename) {
        String contentType = mediaService.getContentType(filename);
        if (contentType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", contentType);
        return new ResponseEntity<>(mediaService.getFile(filename),
                headers,
                HttpStatus.OK);
    }
    //CHECKSTYLE:ON
}
