package ru.panfio.telescreen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Media;
import ru.panfio.telescreen.repository.MediaRepository;

import java.time.LocalDateTime;

@Service
public class MediaService {

    //CHECKSTYLE:OFF
    private final MediaRepository repo;

    private final ObjectStorage objectStorage;

    @Autowired
    public MediaService(MediaRepository repo, ObjectStorage objectStorage) {
        this.repo = repo;
        this.objectStorage = objectStorage;
    }

    public String getContentType(String filename) {
        return objectStorage.getContentType(filename);
    }

    public byte[] getFile(String filename) {
        return objectStorage.getByteArray(filename);
    }

    public Iterable<Media> getAllMediaRecords() {
        return repo.findAll();
    }

    public Iterable<Media> getMediaRecordsByPeriod(
            LocalDateTime from, LocalDateTime to) {
        return repo.getAllBetweenDates(from, to);
    }

    //CHECKSTYLE:ON
}

