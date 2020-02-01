package ru.panfio.telescreen.handler.dao;

import ru.panfio.telescreen.handler.model.Wellbeing;

import java.util.List;

public interface WellbeingDao {
    /**
     * Gets activities from the database.
     *
     * @return activity list
     */
    List<Wellbeing> getActivities();
}
