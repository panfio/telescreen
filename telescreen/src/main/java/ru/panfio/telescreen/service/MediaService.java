package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Media;
import ru.panfio.telescreen.repository.MediaRepository;
import ru.panfio.telescreen.service.util.DateWizard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MediaService implements Processing {

    private final MediaRepository mediaRepository;
    private final ObjectStorage objectStorage;
    private final DateWizard dateWizard;

    /**
     * Constructor.
     *
     * @param mediaRepository repo
     * @param objectStorage   storage
     * @param dateWizard      date helper
     */
    @Autowired
    public MediaService(MediaRepository mediaRepository,
                        ObjectStorage objectStorage,
                        DateWizard dateWizard) {
        this.mediaRepository = mediaRepository;
        this.objectStorage = objectStorage;
        this.dateWizard = dateWizard;
    }

    /**
     * @param filename path
     * @return content type
     */
    public String getContentType(String filename) {
        return objectStorage.contentType(filename);
    }

    /**
     * Returns file from object storage.
     *
     * @param filename path
     * @return file
     */
    public byte[] getFile(String filename) {
        return objectStorage.getByteArray(filename);
    }

    /**
     * Finds and returns all media records from period.
     *
     * @return records
     */
    public Iterable<Media> getAllMediaRecords() {
        return mediaRepository.findAll();
    }

    /**
     * Finds and returns records from period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<Media> getMediaRecordsByPeriod(
            LocalDateTime from, LocalDateTime to) {
        return mediaRepository.findByCreatedBetween(from, to);
    }

    /**
     * Save Media records into database.
     */
    public void processMediaRecords() {
        List<Media> fileList = new ArrayList<>();
        for (String path : objectStorage.listAllObjects()) {
            if (!path.startsWith("media")) {
                continue;
            }
            fileList.add(createMedia(path));
        }
        saveMediaRecords(fileList);
        log.info("Processing media records complete");
    }

    /**
     * Creates media record.
     *
     * @param path file patch.
     * @return media record
     */
    private Media createMedia(String path) {
        Media record = new Media();
        record.setPath(path);
        record.setType(getMediaType(path));
        record.setCreated(dateWizard.creationTime(path));
        return record;
    }

    private String getMediaType(String path) {
        try {
            return path.substring(
                    path.indexOf('/') + 1,
                    path.lastIndexOf('/'));
        } catch (Exception e) {
            return "other";
        }
    }
    /**
     * Saves media records in the database.
     *
     * @param records list of records
     */
    public void saveMediaRecords(List<Media> records) {
        //only existing files are stored in the database
        mediaRepository.deleteAll();
        mediaRepository.saveAll(records);
    }

    @Override
    public void process() {
        processMediaRecords();
    }
}

