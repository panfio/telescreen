package ru.panfio.telescreen.handler.model.autotimer;

import lombok.Data;

import java.util.List;

@Data
public class Activity {
    private String name;
    private List<TimeEntry> time_entries;
}
