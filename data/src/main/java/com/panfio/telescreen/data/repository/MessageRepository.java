package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    /**
     * {@inheritDoc}
     */
    List<Message> findByCreatedBetween(Instant startDate,
                                       Instant endDate);

    /**
     * {@inheritDoc}
     */
    Message findByLegacyIDAndCreated(String legacyID,
                                     Instant created);
}
