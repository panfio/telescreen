package ru.panfio.telescreen.handler.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.YouTube;
import ru.panfio.telescreen.handler.util.Json;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class YouTubeVideoService implements Processing { //TODO create VideoEntity
    private static final String YOUTUBE_EXPORT_PATH =
            "google/YouTube/history/watch-history.json";
    private final MessageBus messageBus;
    private final ObjectStorage objectStorage;

    public YouTubeVideoService(ObjectStorage objectStorage,
                               MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
    }

    @Override
    public void process() {
        log.info("Start processing YouTube history");
        var stream = objectStorage.getInputStream(YOUTUBE_EXPORT_PATH);
        if (stream == null) {
            log.warn("File not found. Put watch-history.json "
                    + "in google/YouTube/history/");
            return;
        }

        List<YouTube> youTubes = parseExport(stream);
        youTubes.forEach(e -> {
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
            var mapper = new ObjectMapper();
            return mapper.readValue(
                    stream,
                    new TypeReference<List<YouTube>>() {
                    });
        } catch (Exception e) {
            log.warn("Parse error " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public String name() {
        return "video";
    }
}
