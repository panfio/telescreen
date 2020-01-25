package ru.panfio.telescreen.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.YouTube;
import ru.panfio.telescreen.repository.YouTubeRepository;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoService implements Processing { //TODO create VideoEntity

    private final YouTubeRepository youTubeRepository;
    private final ObjectStorage objectStorage;

    /**
     * Constructor.
     *
     * @param youTubeRepository repo
     * @param objectStorage     repo
     */
    public VideoService(YouTubeRepository youTubeRepository,
                        ObjectStorage objectStorage) {
        this.youTubeRepository = youTubeRepository;
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
        List<YouTube> filtered = yt.stream().map(e -> {
            e.setId(e.getTime().getEpochSecond());
            e.setTitle(e.getTitle().substring("Watched ".length()));
            return e;
        }).collect(Collectors.toList());
        saveYouTubeRecords(filtered);
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

    /**
     * Saves view records in the database.
     *
     * @param records list of records
     */
    public void saveYouTubeRecords(List<YouTube> records) {
        youTubeRepository.saveAll(records);
    }

    @Override
    public void process() {
        processYouTubeHistory();
    }
}
