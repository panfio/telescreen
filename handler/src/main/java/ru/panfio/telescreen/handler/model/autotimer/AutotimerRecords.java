package ru.panfio.telescreen.handler.model.autotimer;

import lombok.Data;
import ru.panfio.telescreen.handler.model.Autotimer;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AutotimerRecords {
    private List<Activity> activities;

    /**
     * Collects AutoTimer records.
     *
     * @return Autotimer list
     */
    public List<Autotimer> collectAutotimers() {
        return activities.stream()
                .map(Activity::collectAutotimers)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
