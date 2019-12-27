package ru.panfio.telescreen.service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public interface ObjectStorage {
    //CHECKSTYLE:OFF
    List<String> getListOfFileNames();

    String getContentType(String filename);

    byte[] getByteArray(String filename);

    InputStream getInputStream(String filename);

    String saveFileInTempFolder(String filename);

    LocalDateTime getCreatedTime(String filename)
            throws IllegalArgumentException;
    //CHECKSTYLE:ON
}
