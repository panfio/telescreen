package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.Media;
import ru.panfio.telescreen.repository.MediaRepository;
import ru.panfio.telescreen.service.util.DateWizard;

import java.time.Instant;

@Slf4j
@Service
public class MediaService implements Processing {

    @Autowired //todo remove
    private MediaRepository mediaRepository;
    private final MessageBus messageBus;
    private final ObjectStorage objectStorage;
    private final DateWizard dateWizard;

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param objectStorage storage
     * @param dateWizard    date helper
     */
    public MediaService(ObjectStorage objectStorage,
                        DateWizard dateWizard,
                        MessageBus messageBus) {
        this.messageBus = messageBus;
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
     * Finds and returns records from period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<Media> getMediaRecordsByPeriod(
            Instant from, Instant to) {
        return mediaRepository.findByCreatedBetween(from, to);
    }

    /**
     * Save Media records into database.
     */
    public void processMediaRecords() {
        for (String path : objectStorage.listAllObjects()) {
            if (!path.startsWith("media")) {
                continue;
            }
            messageBus.send("media", createMedia(path));
        }
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

    @Override
    public void process() {
        processMediaRecords();
    }
}

