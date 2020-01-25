package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.Media;

import java.time.Instant;
import java.util.List;


public interface MediaRepository extends MongoRepository<Media, String> {

    /**
     * {@inheritDoc}
     */
    List<Media> findByCreatedBetween(Instant startDate,
                                     Instant endDate);
}
