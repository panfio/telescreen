package ru.panfio.telescreen.handler.dao;

import ru.panfio.telescreen.handler.model.MiFitActivity;

import java.util.List;

public interface MiFitDao {
    /**
     * Gets activities from the database.
     *
     * @return activity list
     */
    List<MiFitActivity> getActivities();
}
