package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.Media;

import java.time.LocalDateTime;
import java.util.List;


public interface MediaRepository extends MongoRepository<Media, String> {

    /**
     * {@inheritDoc}
     */
    List<Media> findByCreatedBetween(LocalDateTime startDate,
                                     LocalDateTime endDate);
}
