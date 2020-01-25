package ru.panfio.telescreen.dao;

import ru.panfio.telescreen.model.Wellbeing;

import java.util.List;

public interface WellbeingDao {
    /**
     * Gets activities from the database.
     *
     * @return activity list
     */
    List<Wellbeing> getActivities();
}
