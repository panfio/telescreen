package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.TimeLog;

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
