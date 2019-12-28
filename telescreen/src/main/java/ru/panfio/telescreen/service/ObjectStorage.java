package ru.panfio.telescreen.service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public interface ObjectStorage {

    /**
     * Returns list of filenames in bucket.
     *
     * @return list of filenames or empty list
     */
    List<String> listAllObjects();

    /**
     * Gets file content type.
     *
     * @param filename filename
     * @return contentType or null
     */
    String contentType(String filename);

    /**
     * Returns the byte array of the file.
     *
     * @param filename filename
     * @return byte array or null if file not found
     */
    byte[] getByteArray(String filename);

    /**
     * Returns InputStream of the file.
     *
     * @param filename filename
     * @return inputStream or null file not found
     */
    InputStream getInputStream(String filename);

    /**
     * Downloads file in the temp directory.
     *
     * @param filename filename
     * @return path if success or else null
     */
    String saveInTmpFolder(String filename);

    /**
     * Returns file creation time.
     *
     * @param filename path
     * @return creation time
     * @throws IllegalArgumentException when filename is null
     */
    LocalDateTime getCreatedTime(String filename)
            throws IllegalArgumentException;

}
