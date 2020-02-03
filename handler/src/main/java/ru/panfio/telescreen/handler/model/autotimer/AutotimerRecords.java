package ru.panfio.telescreen.handler.model.autotimer;

import lombok.Data;
import ru.panfio.telescreen.handler.model.Autotimer;

import java.util.ArrayList;
import java.util.List;

@Data
public class AutotimerRecords {
    private List<Activity> activities;

    /**
     * Collects AutoTimer records.
     *
     * @return Autotimer list
     */
    public List<Autotimer> collectAutotimers() {
        List<Autotimer> autotimers = new ArrayList<>();
        for (Activity activity : activities) {
            autotimers.addAll(activity.collectAutotimers());
        }
        return autotimers;
    }
}
