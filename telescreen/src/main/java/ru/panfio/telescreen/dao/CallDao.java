package ru.panfio.telescreen.dao;

import ru.panfio.telescreen.model.Call;

import java.util.List;

public interface CallDao {
    /**
     * Gets call history from the database.
     *
     * @return activity list
     */
    List<Call> getPhoneCalls();
}
