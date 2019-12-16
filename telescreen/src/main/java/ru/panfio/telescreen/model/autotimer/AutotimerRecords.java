package ru.panfio.telescreen.model.autotimer;

import java.util.List;

public class AutotimerRecords {

    private List<Activity> activities;

    public AutotimerRecords(List<Activity> activities) {
        this.activities = activities;
    }

    public AutotimerRecords() {
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

}
