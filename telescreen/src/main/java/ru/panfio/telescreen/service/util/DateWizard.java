package ru.panfio.telescreen.service.util;

import java.time.LocalDateTime;

public interface DateWizard {

    /**
     * Returns creation time of the object.
     *
     * @param path file path
     * @return file's creation time
     */
    LocalDateTime creationTime(String path);

    /**
     * Finds and return LocalDateTime from filename or null if not found.
     *
     * @param path file path
     * @return localDateTime can be null
     */
    LocalDateTime dateFromPath(String path);

}
