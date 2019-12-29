package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.ListenRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface ListenRecordRepository
        extends CrudRepository<ListenRecord, Long> {

    /**
     * {@inheritDoc}
     */
    List<ListenRecord> findByListenTimeBetween(LocalDateTime startDate,
            LocalDateTime endDate);
}
