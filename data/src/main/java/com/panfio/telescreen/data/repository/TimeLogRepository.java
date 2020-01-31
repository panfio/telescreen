package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.TimeLog;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.time.Instant;
import java.util.List;

public interface TimeLogRepository
        extends MongoRepository<TimeLog, String> {

    /**
     * {@inheritDoc}
     */
    List<TimeLog> findByStartDateBetween(Instant startDate,
                                         Instant endDate);
}
