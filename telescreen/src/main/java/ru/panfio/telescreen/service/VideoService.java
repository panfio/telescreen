package ru.panfio.telescreen.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.YouTube;
import ru.panfio.telescreen.repository.YouTubeRepository;
import ru.panfio.telescreen.util.Json;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VideoService implements Processing { //TODO create VideoEntity
    @Autowired //todo delete
    private YouTubeRepository youTubeRepository;
    private final MessageBus messageBus;
    private final ObjectStorage objectStorage;

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param objectStorage     repo
     */
    public VideoService(ObjectStorage objectStorage,
                        MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
    }

    /**
     * Processing YouTube history from Google export.
     */
    public void processYouTubeHistory() {
        log.info("Start processing YouTube history");
        String filename = "google/YouTube/history/watch-history.json";
        InputStream stream = objectStorage.getInputStream(filename);
        if (stream == null) {
            log.warn("File not found. Put watch-history.json "
                    + "in google/YouTube/history/");
            return;
        }

        List<YouTube> yt = parseExport(stream);
        yt.forEach(e -> {
            e.setId(e.getTime().getEpochSecond());
            e.setTitle(e.getTitle().substring("Watched ".length()));
            messageBus.send("video", Json.toJson(e));
        });
        log.info("End processing YouTube history");
    }

    /**
     * Parse export file.
     *
     * @param stream input stream
     * @return entities list
     */
    private List<YouTube> parseExport(InputStream stream) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    stream,
                    new TypeReference<List<YouTube>>() {
                    });
        } catch (Exception e) {
            log.warn("Parse error " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Finds and returns records from period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<YouTube> getYouTubeRecordsBetweenDates(
            Instant from, Instant to) {
        return youTubeRepository.findByTimeBetween(from, to);
    }

    @Override
    public void process() {
        processYouTubeHistory();
    }
}
