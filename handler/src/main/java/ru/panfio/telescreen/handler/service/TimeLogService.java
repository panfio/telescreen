package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.TimeLog;
import ru.panfio.telescreen.handler.model.timesheet.TimesheetExport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class TimeLogService implements Processing {
    private final MessageBus messageBus;
    private final ObjectStorage objectStorage;

    public TimeLogService(ObjectStorage objectStorage,
                          MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
    }

    @Override
    public void process() {
        String lastBackup = findLastBackupFile();
        log.info("Processing " + lastBackup);
        TimesheetExport export = parseExportFile(lastBackup);
        if (export == null) {
            log.error("File is invalid");
            return;
        }
        List<TimeLog> timeLogs = export.getTimeLogs();
        timeLogs.forEach(this::sendMessage);
        log.info("End processing Timesheet records");
    }

    private void sendMessage(TimeLog activity) {
        messageBus.send("timelog", activity);
    }

    private TimesheetExport parseExportFile(final String filename) {
        log.info(filename);
        return unmarshallXml(
                TimesheetExport.class,
                objectStorage.getInputStream(filename));
    }

    private String findLastBackupFile() {
        return objectStorage.listObjects(this::isaTimesheetExportFile)
                .stream()
                .max(String::compareToIgnoreCase)
                .orElseThrow(
                        () -> new NoSuchElementException("No files found"));
    }

    private boolean isaTimesheetExportFile(String filename) {
        return filename.startsWith("timesheet/TimesheetBackup");
    }

    @SuppressWarnings("unchecked")
    public <T> T unmarshallXml(Class<T> clazz, InputStream stream) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (T) jaxbUnmarshaller.unmarshal(stream);
        } catch (Exception e) {
            log.error("Parse error " + clazz.getName() + " " + e.getMessage());
            return null;
        }
    }

    @Override
    public String name() {
        return "timelog";
    }
}
