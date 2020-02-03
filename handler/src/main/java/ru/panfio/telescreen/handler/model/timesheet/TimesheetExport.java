package ru.panfio.telescreen.handler.model.timesheet;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * Collects task tags.
     *
     * @param task task
     * @return tag list
     */
    public List<String> getTaskTags(final Task task) {
        final Map<String, String> tags = this.tagsMap();
        return this.getTaskTags().getTaskTags()
                .stream()
                .filter(taskTag -> taskTag.getTaskId().equals(task.getTaskId()))
                .map(el -> tags.get(el.getTagId()))
                .collect(Collectors.toList());
    }

    private Map<String, String> tagsMap() {
        Map<String, String> tags = new HashMap<>();
        this.getTags().getTags().forEach(
                tag -> tags.put(tag.getTagId(), tag.getName())
        );
        return tags;
    }
}
