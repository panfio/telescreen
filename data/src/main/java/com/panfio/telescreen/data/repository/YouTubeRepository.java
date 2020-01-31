package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.YouTube;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.time.Instant;
import java.util.List;

public interface YouTubeRepository extends MongoRepository<YouTube, Long> {

    /**
     * {@inheritDoc}
     */
    List<YouTube> findByTimeBetween(Instant startTime,
                                    Instant endTime);

}
