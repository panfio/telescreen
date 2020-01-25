package ru.panfio.telescreen.dao;

import ru.panfio.telescreen.model.MiFitActivity;

import java.util.List;

public interface MiFitDao {
    /**
     * Gets activities from the database.
     *
     * @return activity list
     */
    List<MiFitActivity> getActivities();
}
