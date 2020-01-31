package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.TimeLog;
import ru.panfio.telescreen.model.timesheet.Task;
import ru.panfio.telescreen.model.timesheet.TimesheetExport;
import ru.panfio.telescreen.repository.TimeLogRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TimeLogService implements Processing {
    @Autowired  //todo remove
    private TimeLogRepository timeLogRepository;
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
     * Finds and returns TimeLog records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<TimeLog> getTimeLogRecordsBetweenDates(
            Instant from, Instant to) {
        return timeLogRepository.findByStartDateBetween(from, to);
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
        //tags are collected here to increase performance
        Map<String, String> tags = getTags(export);
        for (Task task : export.getTasks().getTasks()) {
            List<String> taskTags = getTaskTags(export, tags, task);
            TimeLog activity = createTimeLog(task, taskTags);
            messageBus.send("timelog", activity);
        }
        log.info("End processing Timesheet records");
        return true;
    }

    /**
     * Gets all tags.
     *
     * @param export export
     * @return tags
     */
    private Map<String, String> getTags(TimesheetExport export) {
        Map<String, String> tags = new HashMap<>();
        export.getTags().getTags().forEach(tag -> {
            tags.put(tag.getTagId(), tag.getName());
        });
        return tags;
    }

    /**
     * Collects task tags.
     *
     * @param export export
     * @param tags   tags
     * @param task   task
     * @return tags
     */
    private List<String> getTaskTags(final TimesheetExport export,
                                     final Map<String, String> tags,
                                     final Task task) {
        return export.getTaskTags().getTaskTags().stream()
                .filter(
                        taskTag -> taskTag.getTaskId()
                                .equals(task.getTaskId()))
                .map(el -> tags.get(el.getTagId()))
                .collect(Collectors.toList());
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
        TimeLog tl = new TimeLog();
        tl.setId(task.getTaskId());
        tl.setDescription(task.getDescription());
        tl.setStartDate(task.getStartDate());
        tl.setEndDate(task.getEndDate());
        tl.setLocation(task.getLocation());
        tl.setFeeling(task.getFeeling());
        tl.setTags(taskTags);
        return tl;
    }

    /**
     * Gets the newest backup file.
     *
     * @return file path
     */
    public String findLastBackupFile() {
        return objectStorage.listAllObjects()
                .stream()
                .filter(s -> s.startsWith("timesheet/TimesheetBackup"))
                .max(String::compareToIgnoreCase)
                .orElseThrow(
                        () -> new NoSuchElementException("No files found"));
    }

    /**
     * Unmarshal given xml from input stream.
     *
     * @param clazz  class
     * @param stream input stream
     * @param <T>    type
     * @return //todo
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
}
