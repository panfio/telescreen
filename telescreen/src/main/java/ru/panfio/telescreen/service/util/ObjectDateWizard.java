package ru.panfio.telescreen.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.service.ObjectStorage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ObjectDateWizard implements DateWizard {

    @Value("${server.zoneOffset}")
    private int zoneOffset;
    private static final Map<String, String> DATE_REGEXPS =
            new HashMap<String, String>();

    //Please cover new cases in the test
    static {
        DATE_REGEXPS.put(".*(\\d{8}_\\d{6}).*", "yyyyMMdd_HHmmss");
        DATE_REGEXPS.put(".*(\\d{8}-\\d{6}).*", "yyyyMMdd-HHmmss");
        DATE_REGEXPS.put(
                ".*(\\d{4}-\\d{1,2}-\\d{1,2}-\\d{1,2}-\\d{2}-\\d{2}).*",
                "yyyy-MM-dd-HH-mm-ss");
        DATE_REGEXPS.put(
                ".*(\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}-\\d{2}-\\d{2}).*",
                "yyyy-MM-dd HH-mm-ss");
        DATE_REGEXPS.put(
                ".*(\\d{4}-\\d{1,2}-\\d{1,2}_\\d{6}).*",
                "yyyy-MM-dd_HHmmss");
    }

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
    public Instant creationTime(String path) {
        if (path == null) {
            throw new IllegalArgumentException();
        }
        Instant dateFromPath = dateFromPath(path);
        if (dateFromPath != null) {
            return dateFromPath;
        }
        Instant dateFromObjectInfo = objectStorage.getCreatedTime(path);
        if (dateFromObjectInfo != null) {
            return dateFromObjectInfo;
        }
        //todo get date from meta like Iphone photos
        // (may be significantly slower)
        return Instant.now(); //todo
    }

    /**
     * Determine pattern matching with the given date string.
     * Returns null if format is unknown.
     *
     * @param dateString The date string
     * @return regexp, or null if format is unknown.
     */
    public static String determineDateFormat(String dateString) {
        for (String regexp : DATE_REGEXPS.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return regexp;
            }
        }
        return null; // Unknown format.
    }

    /**
     * Parse the given date string to date object and return
     * a date instance based on the given date string.
     *
     * @param dateString The date string to be parsed to date object.
     * @return The parsed date object.
     */
    public static LocalDateTime parse(String dateString) {
        String regexp = determineDateFormat(dateString);
        if (regexp == null) {
            log.info("Unknown date format.");
            return null;
        }
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(dateString);
        if (matcher.find()) {
            return parse(matcher.group(1), DATE_REGEXPS.get(regexp));
        }
        return null;
    }

    /**
     * Validate the actual date of the given date string
     * based on the given date format pattern and
     * return a date instance based on the given date string.
     *
     * @param dateString The date string.
     * @param dateFormat The date format pattern
     * @return The parsed date object.
     */
    public static LocalDateTime parse(String dateString, String dateFormat) {
        try {
            return LocalDateTime.parse(
                    dateString, DateTimeFormatter.ofPattern(dateFormat));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Finds and return creation time from the filename or null if not found.
     *
     * @param path file path
     * @return time can be null
     */
    @Override
    public Instant dateFromPath(String path) {
        LocalDateTime dateFromPath = parse(path);
        if (dateFromPath != null) {
            return dateFromPath.toInstant(
                    ZoneOffset.ofHours(zoneOffset));
        }
        return null;
    }

}
