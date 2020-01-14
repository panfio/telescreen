package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.CallRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface CallRecordRepository
        extends MongoRepository<CallRecord, String> {

    /**
     * {@inheritDoc}
     */
    List<CallRecord> findByDateBetween(LocalDateTime startDate,
                                       LocalDateTime endDate);
}
