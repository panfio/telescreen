package ru.panfio.telescreen.model.timesheet;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Data
@ToString
@XmlRootElement(name="timesheet")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimesheetExport {
    @XmlElement(name="tags")
    private Tags tags;
    @XmlElement(name = "tasks")
    private Tasks tasks;
    @XmlElement(name = "taskTags")
    private TaskTags taskTags;
}
