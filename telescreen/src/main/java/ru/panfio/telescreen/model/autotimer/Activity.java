package ru.panfio.telescreen.model.autotimer;

import java.util.List;


public class Activity {
    private String name;
    private List<TimeEntry> time_entries;

    public Activity(String name, List<TimeEntry> time_entries) {
        this.name = name;
        this.time_entries = time_entries;
    }

    public Activity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TimeEntry> getTime_entries() {
        return time_entries;
    }

    public void setTime_entries(List<TimeEntry> time_entries) {
        this.time_entries = time_entries;
    }

}
