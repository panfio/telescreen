package ru.panfio.telescreen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

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

//    /**
//     * Establishes a database connection.
//     *
//     * @param filename filename
//     * @return connection or null if an error occurred
//     * @throws FileNotFoundException w
//     */
//    public Connection connectSQLite(String filename)
//            throws FileNotFoundException {
//        Connection conn = null;
//        String path = objectStorage.saveInTmpFolder(filename);
//        if (path == null) {
//            log.warn("File not found. Put {}", filename);
//            throw new FileNotFoundException();
//        }
//        try {
//            conn = DriverManager.getConnection("jdbc:sqlite:" + path);
//        } catch (SQLException e) {
//            log.error(e.getMessage());
//        }
//        return conn;
//    }

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
