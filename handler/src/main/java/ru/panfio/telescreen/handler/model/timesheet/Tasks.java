package ru.panfio.telescreen.handler.model.timesheet;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Tasks {
    @XmlElement(name="task")
    private List<Task> tasks;
}
