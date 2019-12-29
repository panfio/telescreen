package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.TimeLog;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeLogRepository extends CrudRepository<TimeLog, String> {

    /**
     * {@inheritDoc}
     */
    List<TimeLog> findByStartDateBetween(LocalDateTime startDate,
                                         LocalDateTime endDate);
}
