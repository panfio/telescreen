package ru.panfio.telescreen.handler.service.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface DateWizard {

    /**
     * Converts LocaDateTime to instant at default timezone.
     * @param time LocaDateTime
     * @return Instant
     */
    static Instant toInstant(LocalDateTime time) {
        return time.toInstant(
                ZoneId.systemDefault().getRules().getOffset(Instant.now())
        );
    }

    /**
     * Returns creation time of the object.
     *
     * @param path file path
     * @return file's creation time
     */
    Instant creationTime(String path);

    /**
     * Finds and return LocalDateTime from filename or null if not found.
     *
     * @param path file path
     * @return localDateTime can be null
     */
    Instant dateFromPath(String path);

}
