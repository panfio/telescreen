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
    private static final String DEFAULT_TYPE = "other";
    private final MessageBus messageBus;
    private final ObjectStorage objectStorage;
    private final DateWizard dateWizard;

    public MediaService(ObjectStorage objectStorage,
                        DateWizard dateWizard,
                        MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
        this.dateWizard = dateWizard;
    }

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

    @Override
    public void process() {
        for (var path : getMediaFiles()) {
            Instant creationTime = getCreatedTime(path);
            Media media = createMedia(path, creationTime);
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

    private Instant getCreatedTime(String path) {
        return dateWizard.creationTime(path);
    }

    private Media createMedia(String path,
                              Instant creationTime) {
        return Media.builder()
                .path(path)
                .type(getMediaType(path))
                .created(creationTime)
                .build();

    }

    private String getMediaType(String path) {
        try {
            return path.substring(
                    path.indexOf('/') + 1,
                    path.lastIndexOf('/'));
        } catch (Exception e) {
            return DEFAULT_TYPE;
        }
    }

    @Override
    public String name() {
        return "media";
    }
}

