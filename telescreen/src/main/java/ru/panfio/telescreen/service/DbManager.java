package ru.panfio.telescreen.service;

import java.io.FileNotFoundException;
import java.sql.Connection;

public interface DbManager {

    /**
     * Establishes a database connection.
     *
     * @param filename filename
     * @return connection or null if an error occurred
     * @throws FileNotFoundException w
     */
    Connection connectSQLite(String filename)
            throws FileNotFoundException;
}
