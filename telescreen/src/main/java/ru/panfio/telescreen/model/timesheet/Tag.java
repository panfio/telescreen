package ru.panfio.telescreen.model.timesheet;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Tag {
    private String tagId;
    private String name;
    private boolean deleted;
}
