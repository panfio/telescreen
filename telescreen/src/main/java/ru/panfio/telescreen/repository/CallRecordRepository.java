package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.Call;

import java.time.Instant;
import java.util.List;

public interface CallRecordRepository
        extends MongoRepository<Call, String> {

    /**
     * {@inheritDoc}
     */
    List<Call> findByDateBetween(Instant startDate,
                                 Instant endDate);
}
