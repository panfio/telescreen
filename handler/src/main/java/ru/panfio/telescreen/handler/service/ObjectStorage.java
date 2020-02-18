package ru.panfio.telescreen.handler.service;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;

public interface ObjectStorage {

    /**
     * Returns list of filenames in bucket.
     *
     * @return list of filenames or empty list
     */
    List<String> listAllObjects();

    List<String> listObjects(Predicate<String> predicate);

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
    Instant getCreatedTime(String filename)
            throws IllegalArgumentException;

    /**
     * Gets text content from the file.
     *
     * @param filename filename
     * @param charset  text encoding
     * @return text
     */
    String getContent(String filename, String charset);
}
