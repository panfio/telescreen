package ru.panfio.telescreen.model.timesheet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class TaskTags {
    @XmlElement(name="taskTag")
    private List<TaskTag> taskTags;

    public TaskTags(List<TaskTag> taskTags) {
        this.taskTags = taskTags;
    }

    public TaskTags() {
    }

    public List<TaskTag> getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(List<TaskTag> taskTags) {
        this.taskTags = taskTags;
    }
}
