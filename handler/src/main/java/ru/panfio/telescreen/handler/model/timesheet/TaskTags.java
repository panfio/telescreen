package ru.panfio.telescreen.handler.model.timesheet;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskTags {
    @XmlElement(name="taskTag")
    private List<TaskTag> taskTags;
}
