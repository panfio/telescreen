package ru.panfio.telescreen.handler.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panfio.telescreen.handler.service.MediaService;

@CrossOrigin
@RestController
@RequestMapping("/media")
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
     * Returns file.
     *
     * @param filename file path
     * @return file
     */
    @GetMapping("/file")
    public ResponseEntity<byte[]> getFileByFileName(
            @RequestParam(value = "filename") String filename) {
        String contentType = service.getContentType(filename);
        if (contentType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var headers = new HttpHeaders();
        headers.set("Content-type", contentType);
        return new ResponseEntity<>(
                service.getFile(filename),
                headers,
                HttpStatus.OK);
    }
}
