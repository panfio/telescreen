package ru.panfio.telescreen.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.service.MediaService;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MediaController {

    private final MediaService service;

    /**
     * Constructor.
     *
     * @param service service
     */
    public MediaController(MediaService service) {
        this.service = service;
    }

    /**
     * Get all media records.
     *
     * @return media records
     */
    @Deprecated
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all media records")
    @GetMapping("/media/all")
    ResponseEntity findAll() {
        return new ResponseEntity<>(
                service.getAllMediaRecords(),
                HttpStatus.OK);
    }

    /**
     * Processing media files.
     *
     * @return ok
     */
    @ApiOperation(value = "Processing media files")
    @GetMapping("/media/process")
    public ResponseEntity processMediaRecords() {
        if (service.processMediaRecords()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Get media records by period.
     *
     * @param from time
     * @param to   time
     * @return media records
     */
    @ApiOperation(value = "Get media records by period")
    @GetMapping("/media")
    ResponseEntity findAutotimerRecordsByPeriod(
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to) {
        LocalDateTime t1 = LocalDateTime.parse(from);
        LocalDateTime t2 = LocalDateTime.parse(to);
        return new ResponseEntity<>(
                service.getMediaRecordsByPeriod(t1, t2),
                HttpStatus.OK);
    }

    /**
     * Returns file.
     *
     * @param filename file path
     * @return file
     */
    @GetMapping("/media/file")
    public ResponseEntity<byte[]> getFileByFileName(
            @RequestParam(value = "filename") String filename) {
        String contentType = service.getContentType(filename);
        if (contentType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", contentType);
        return new ResponseEntity<>(service.getFile(filename),
                headers,
                HttpStatus.OK);
    }
}
