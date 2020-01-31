package com.panfio.telescreen.data.repository;



import com.panfio.telescreen.model.Media;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;


public interface MediaRepository extends MongoRepository<Media, String> {

    /**
     * {@inheritDoc}
     */
    List<Media> findByCreatedBetween(Instant startDate,
                                     Instant endDate);
}
