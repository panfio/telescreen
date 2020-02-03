package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.Media;
import ru.panfio.telescreen.handler.service.util.DateWizard;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class MediaService implements Processing {
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
     * Save Media records into database.
     */
    public void processMediaRecords() {
        for (var path : getMediaFiles()) {
            Media media = createMedia(path);
            messageBus.send("media", media);
        }
        log.info("Processing media records complete");
    }

    private List<String> getMediaFiles() {
        return objectStorage.listObjects(this::isMedia);
    }

    private boolean isMedia(String path) {
        return path.startsWith("media");
    }

    /**
     * Creates media record.
     *
     * @param path file patch.
     * @return media record
     */
    private Media createMedia(String path) {
        return Media.builder()
                .path(path)
                .type(getMediaType(path))
                .created(getCreatedTime(path))
                .build();

    }

    private Instant getCreatedTime(String path) {
        return dateWizard.creationTime(path);
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

    @Override
    public String name() {
        return "media";
    }
}

