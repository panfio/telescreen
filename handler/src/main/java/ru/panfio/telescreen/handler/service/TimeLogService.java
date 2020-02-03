package ru.panfio.telescreen.handler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.handler.model.TimeLog;
import ru.panfio.telescreen.handler.model.timesheet.Task;
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

    /**
     * Constructor.
     *
     * @param messageBus    message bus
     * @param objectStorage service
     */
    public TimeLogService(ObjectStorage objectStorage,
                          MessageBus messageBus) {
        this.messageBus = messageBus;
        this.objectStorage = objectStorage;
    }

    /**
     * Processing Timesheet backup file and save them as TimeLog records.
     *
     * @return true if operation is successful
     */
    public boolean processTimesheetRecords() {
        String lastBackup = findLastBackupFile();
        log.info("Processing " + lastBackup);
        TimesheetExport export = parseExportFile(lastBackup);
        if (export == null) {
            log.error("File is invalid");
            return false;
        }

        for (Task task : export.getTasks().getTasks()) {
            List<String> taskTags = export.getTaskTags(task);
            TimeLog activity = createTimeLog(task, taskTags);
            messageBus.send("timelog", activity);
        }
        log.info("End processing Timesheet records");
        return true;
    }

    /**
     * Parsing export file.
     *
     * @param filename filename
     * @return export
     */
    private TimesheetExport parseExportFile(final String filename) {
        log.info(filename);
        return unmarshallXml(
                TimesheetExport.class,
                objectStorage.getInputStream(filename));
    }

    /**
     * Creates a timelog entity.
     *
     * @param task     current task
     * @param taskTags tags
     * @return entity
     */
    private TimeLog createTimeLog(final Task task,
                                  final List<String> taskTags) {
        return TimeLog.builder()
                .id(task.getTaskId())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .location(task.getLocation())
                .feeling(task.getFeeling())
                .tags(taskTags)
                .build();
    }

    /**
     * Gets the newest backup file.
     *
     * @return file path
     */
    private String findLastBackupFile() {
        return objectStorage.listObjects(this::isaTimesheetExportFile)
                .stream()
                .max(String::compareToIgnoreCase)
                .orElseThrow(
                        () -> new NoSuchElementException("No files found"));
    }

    private boolean isaTimesheetExportFile(String s) {
        return s.startsWith("timesheet/TimesheetBackup");
    }

    /**
     * Unmarshal given xml from input stream.
     *
     * @param clazz  class
     * @param stream input stream
     * @param <T>    type
     * @return <T>
     */
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
    public void process() {
        processTimesheetRecords();
    }

    @Override
    public String name() {
        return "timelog";
    }
}
