package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.Message;

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
