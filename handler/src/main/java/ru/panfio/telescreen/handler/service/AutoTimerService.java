package ru.panfio.telescreen.handler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.Autotimer;
import ru.panfio.telescreen.handler.model.autotimer.AutotimerRecords;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class AutoTimerService implements Processing {
    private final MessageBus messageBus;
    private final ObjectStorage objectStorage;

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param objectStorage storage
     */
    public AutoTimerService(ObjectStorage objectStorage,
                            MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
    }

    /**
     * Processing AutoTimer records.
     *
     */
    public void processAutotimerRecords() {
        log.info("Processsing Autotimer records");
        for (var filename : getActivityFiles()) {
            //todo processing files in parallel
            AutotimerRecords records = parseActivityFile(filename);
            List<Autotimer> list = records.collectAutotimers();
            list.forEach(this::sendMessage);
        }
        log.info("End processsing Autotimer records");
    }

    private void sendMessage(Autotimer activity) {
        messageBus.send("autotimer", activity);
    }

    private List<String> getActivityFiles() {
        return objectStorage.listObjects(this::isActivityFile);
    }

    private boolean isActivityFile(String filename) {
        return filename.contains("activities");
    }

    /**
     * Parsing activity file.
     *
     * @param filename fillename
     * @return activity list
     */
    private AutotimerRecords parseActivityFile(String filename) {
        try {
            var mapper = new ObjectMapper();
            return mapper.readValue(
                    objectStorage.getInputStream(filename),
                    AutotimerRecords.class);
        } catch (IOException e) {
            log.warn("Autotimer parse error " + filename);
            return new AutotimerRecords();
        }
    }

    @Override
    public void process() {
        processAutotimerRecords();
    }

    @Override
    public String name() {
        return "autotimer";
    }
}
