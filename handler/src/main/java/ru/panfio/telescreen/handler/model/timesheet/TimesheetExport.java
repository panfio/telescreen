package ru.panfio.telescreen.handler.model.timesheet;

import lombok.Data;
import lombok.ToString;
import ru.panfio.telescreen.handler.model.TimeLog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import java.util.stream.Collectors;

@Data
@ToString
@XmlRootElement(name = "timesheet")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimesheetExport {
    @XmlElement(name = "tags")
    private Tags tags;
    @XmlElement(name = "tasks")
    private Tasks tasks;
    @XmlElement(name = "taskTags")
    private TaskTags taskTags;

    private List<String> getTaskTags(final Task task,
                                     Map<String, String> tags) {
        return taskTags.getTaskTags()
                .stream()
                .filter(taskTag -> taskTag.getTaskId().equals(task.getTaskId()))
                .map(el -> tags.get(el.getTagId()))
                .collect(Collectors.toList());
    }

    private Map<String, String> tagsMap() {
        return tags.getTags()
                .stream()
                .collect(Collectors.toMap(
                        Tag::getTagId, Tag::getName, (a, b) -> b));
    }

    public List<TimeLog> getTimeLogs() {
        final Map<String, String> allTags = tagsMap();
        return tasks.getTasks()
                .stream()
                .map(task -> createTimeLog(task, getTaskTags(task, allTags)))
                .collect(Collectors.toList());
    }

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
}
