package ru.panfio.telescreen.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public interface DbManager {

    /**
     * Creates JdbcTemplate for the database access.
     * @param filename database path
     * @return JdbcTemplate
     */
    JdbcTemplate getTemplate(String filename);
}
