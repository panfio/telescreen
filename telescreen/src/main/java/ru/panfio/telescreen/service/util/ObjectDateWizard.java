package ru.panfio.telescreen.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.service.ObjectStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
public class ObjectDateWizard implements DateWizard {

    private final ObjectStorage objectStorage;

    /**
     * Creates with object storage.
     *
     * @param objectStorage storage
     */
    public ObjectDateWizard(ObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
    }

    /**
     * Returns creation time.
     *
     * @param path file path
     * @return file's creation time
     */
    @Override
    public LocalDateTime creationTime(String path) {
        if (path == null) {
            throw new IllegalArgumentException();
        }
        LocalDateTime dateFromPath = dateFromPath(path);
        if (dateFromPath != null) {
            return dateFromPath;
        }
        LocalDateTime dateFromObjectInfo = objectStorage.getCreatedTime(path);
        if (dateFromObjectInfo != null) {
            return dateFromObjectInfo;
        }
        //todo get date from meta like Iphone photos
        // (may be significantly slower)
        return LocalDateTime.now(); //todo
    }

    /**
     * Finds and return LocalDateTime from filename or null if not found.
     *
     * @param path file path
     * @return localDateTime can be null
     */
    @Override
    public LocalDateTime dateFromPath(String path) {
        //TODO find a pretty solution for this ugly code.
        // consider switch/regexp + date validation
        //Cover new cases in test
        String filename = path.substring(path.lastIndexOf("/") + 1);
        String dateInName = "";
        String pattern = "";
        if (filename.startsWith("IMG_")
                || filename.startsWith("VID_")
                || filename.startsWith("Timesheet")) {
            if (filename.startsWith("Timesheet")) {
                pattern = "yyyy-MM-dd_HHmmss";
            } else {
                pattern = "yyyyMMdd_HHmmss";
            }
            int start = filename.indexOf("_") + 1;
            dateInName = filename.substring(start, start + pattern.length());
        } else if (filename.endsWith("-note.m4a")) {
            dateInName = filename.substring(0, filename.lastIndexOf("-"));
            pattern = "yyyy-MM-dd-HH-mm-ss";
        } else if (filename.startsWith("Screenshot")) {
            dateInName = filename.substring(
                    filename.indexOf("2"),
                    filename.lastIndexOf("."));
            if (filename.contains(" ")) {
                pattern = "yyyy-MM-dd HH-mm-ss";
            } else {
                pattern = "yyyyMMdd-HHmmss";
            }
        }
        try {
            return LocalDateTime.parse(
                    dateInName, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

}
