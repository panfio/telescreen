package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.Call;

import java.time.LocalDateTime;
import java.util.List;

public interface CallRecordRepository
        extends MongoRepository<Call, String> {

    /**
     * {@inheritDoc}
     */
    List<Call> findByDateBetween(LocalDateTime startDate,
                                 LocalDateTime endDate);
}
