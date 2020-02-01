package ru.panfio.telescreen.handler.service.util;

import java.time.Instant;

public interface DateWizard {

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
