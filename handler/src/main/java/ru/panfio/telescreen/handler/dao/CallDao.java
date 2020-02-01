package ru.panfio.telescreen.handler.dao;

import ru.panfio.telescreen.handler.model.Call;

import java.util.List;

public interface CallDao {
    /**
     * Gets call history from the database.
     *
     * @return activity list
     */
    List<Call> getPhoneCalls();
}
