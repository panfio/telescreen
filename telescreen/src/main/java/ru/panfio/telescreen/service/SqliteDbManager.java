package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Service
public class SqliteDbManager implements DbManager {

    private final ObjectStorage objectStorage;

    /**
     * Constructor.
     *
     * @param objectStorage storage
     */
    @Autowired
    public SqliteDbManager(ObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
    }

    /**
     * Establishes a database connection.
     *
     * @param filename filename
     * @return connection or null if an error occurred
     * @throws FileNotFoundException w
     */
    public Connection connectSQLite(String filename)
            throws FileNotFoundException {
        Connection conn = null;
        String path = objectStorage.saveInTmpFolder(filename);
        if (path == null) {
            log.warn("File not found. Put {}", filename);
            throw new FileNotFoundException();
        }
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return conn;
    }

}
