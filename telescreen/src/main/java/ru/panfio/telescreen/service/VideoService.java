package ru.panfio.telescreen.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.YouTube;
import ru.panfio.telescreen.repository.YouTubeRepository;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoService { //TODO create VideoEntity

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
        try {
            InputStream stream = objectStorage.getInputStream(filename);
            if (stream == null) {
                log.warn("File not found. Put watch-history.json "
                        + "in google/YouTube/history/");
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            List<YouTube> yt = mapper.readValue(
                    stream,
                    new TypeReference<List<YouTube>>() {
                    });
            List<YouTube> filtered = yt.stream().map(e -> {
                e.setId(e.getTime().toEpochSecond(ZoneOffset.UTC));
                e.setTitle(e.getTitle().substring("Watched ".length()));
                return e;
            }).collect(Collectors.toList());
            saveYouTubeRecords(filtered);
        } catch (Exception e) {
            log.warn("Parse error " + filename);
            e.printStackTrace();
        }
        log.info("End processing YouTube history");
    }

    /**
     * Finds and returns records from period.
     * @param from time
     * @param to time
     * @return records
     */
    public Iterable<YouTube> getYouTubeRecordsBetweenDates(
            LocalDateTime from, LocalDateTime to) {
        return youTubeRepository.getAllBetweenDates(from, to);
    }

    /**
     * Saves view records in the database.
     *
     * @param records list of records
     */
    public void saveYouTubeRecords(List<YouTube> records) {
        youTubeRepository.saveAll(records);
    }
}
