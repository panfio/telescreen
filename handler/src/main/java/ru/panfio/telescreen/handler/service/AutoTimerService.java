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

    public AutoTimerService(ObjectStorage objectStorage,
                            MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
    }

    @Override
    public void process() {
        log.info("Processsing Autotimer records");
        List<String> activityFiles = getActivityFiles();
        activityFiles.parallelStream()
                .forEach(this::handleExportFile);
        log.info("End processsing Autotimer records");
    }

    private void handleExportFile(String filename) {
        AutotimerRecords records = parseActivityFile(filename);
        List<Autotimer> list = records.collectAutotimers();
        list.forEach(this::sendMessage);
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
    public String name() {
        return "autotimer";
    }
}
