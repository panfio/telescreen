package ru.panfio.telescreen.model.timesheet;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="timesheet")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimesheetExport {
    @XmlElement(name="tags")
    private Tags tags;
    @XmlElement(name = "tasks")
    private Tasks tasks;
    @XmlElement(name = "taskTags")
    private TaskTags taskTags;

    public TimesheetExport() {
    }

    public TimesheetExport(Tags tags, Tasks tasks, TaskTags taskTags) {
        this.tags = tags;
        this.tasks = tasks;
        this.taskTags = taskTags;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    public TaskTags getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(TaskTags taskTags) {
        this.taskTags = taskTags;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "TimesheetExport{" +
                ", tags=" + tags +
                ", tass=" + tasks +
                ", taskTags=" + taskTags +
                '}';
    }
}
