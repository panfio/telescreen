package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.CallRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface CallRecordRepository extends CrudRepository<CallRecord, Long> {

    /**
     * {@inheritDoc}
     */
    List<CallRecord> findByDateBetween(LocalDateTime startDate,
                                       LocalDateTime endDate);
}
