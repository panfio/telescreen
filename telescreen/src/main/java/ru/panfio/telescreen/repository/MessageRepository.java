package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    /**
     * {@inheritDoc}
     */
    List<Message> findByCreatedBetween(LocalDateTime startDate,
                                       LocalDateTime endDate);

    /**
     * {@inheritDoc}
     */
    Message findByLegacyIDAndCreated(String legacyID,
                                     LocalDateTime created);
}
