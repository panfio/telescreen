package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.model.TimeLog;
import ru.panfio.telescreen.model.timesheet.Task;
import ru.panfio.telescreen.model.timesheet.TimesheetExport;
import ru.panfio.telescreen.repository.TimeLogRepository;
import ru.panfio.telescreen.service.util.DateWizard;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TimeLogService implements Processing {

    private final TimeLogRepository timeLogRepository;

    private final ObjectStorage objectStorage;

    private final DateWizard dateWizard;

    /**
     * Constructor.
     *
     * @param timeLogRepository repo
     * @param objectStorage     service
     * @param dateWizard        date helper
     */
    public TimeLogService(TimeLogRepository timeLogRepository,
                          ObjectStorage objectStorage,
                          DateWizard dateWizard) {
        this.timeLogRepository = timeLogRepository;
        this.objectStorage = objectStorage;
        this.dateWizard = dateWizard;
    }

    /**
     * Saves timelog records in the database.
     *
     * @param records list of records
     */
    public void saveTimeLogRecords(List<TimeLog> records) {
        timeLogRepository.saveAll(records);
    }

    /**
     * Finds and returns TimeLog records for the period.
     *
     * @param from time
     * @param to   time
     * @return records
     */
    public Iterable<TimeLog> getTimeLogRecordsBetweenDates(
            LocalDateTime from, LocalDateTime to) {
        return timeLogRepository.findByStartDateBetween(from, to);
    }

    /**
     * Processing Timesheet backup file and save them as TimeLog records.
     *
     * @return true if operation is successful
     */
    public boolean processTimesheetRecords() {
        //todo create method that returns file list in folder
        String lastBackup = objectStorage.listAllObjects().stream()
                .filter(s -> s.startsWith("timesheet/TimesheetBackup"))
                .reduce((max, current) -> {
                    //will fall if filename is incorrect
                    LocalDateTime dt = dateWizard.dateFromPath(current);
                    return ((dt != null) && (dt.isAfter(
                            dateWizard.dateFromPath(max))))
                            ? current
                            : max;
                }).orElse("");
        if (lastBackup.equals("")) {
            log.warn("Timesheet backup files not found");
            return false;
        }
        log.info("Processing " + lastBackup);
        TimesheetExport te = unmarshallXml(
                TimesheetExport.class,
                objectStorage.getInputStream(lastBackup));

        Map<String, String> tags = new HashMap<>();
        te.getTags().getTags().forEach(tag -> {
            tags.put(tag.getTagId(), tag.getName());
        });
        List<TimeLog> timeLogs = new ArrayList<>();
        for (Task task : te.getTasks().getTasks()) {
            TimeLog tl = new TimeLog();
            tl.setId(task.getTaskId());
            tl.setDescription(task.getDescription());
            tl.setStartDate(task.getStartDate());
            tl.setEndDate(task.getEndDate());
            tl.setLocation(task.getLocation());
            tl.setFeeling(task.getFeeling());
            List<String> tlTags = te.getTaskTags().getTaskTags().stream()
                    .filter(
                            taskTag -> taskTag.getTaskId()
                                    .equals(task.getTaskId()))
                    .map(el -> tags.get(el.getTagId()))
                    .collect(Collectors.toList());
            tl.setTags(tlTags);
            timeLogs.add(tl);
        }
        saveTimeLogRecords(timeLogs);
        return true;
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
