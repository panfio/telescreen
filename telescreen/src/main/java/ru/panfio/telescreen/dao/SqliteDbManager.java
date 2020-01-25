package ru.panfio.telescreen.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import ru.panfio.telescreen.service.ObjectStorage;

import javax.sql.DataSource;

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
     * Establishes the database connection.
     *
     * @param filename database path
     * @return DataSource
     */
    public DataSource sqliteDataSource(String filename) {
        String path = objectStorage.saveInTmpFolder(filename);
        if (path == null) {
            log.warn("File not found. Put {}", filename);
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:" + path);
        return dataSource;
    }

    /**
     * {@inheritDoc}
     */
    public JdbcTemplate getTemplate(String filename) {
        DataSource dataSource = sqliteDataSource(filename);
        return new JdbcTemplate(dataSource);
    }
}
