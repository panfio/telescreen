package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Call;
import org.springframework.data.mongodb.repository.MongoRepository;


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
