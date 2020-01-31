package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Music;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.time.Instant;
import java.util.List;

public interface MusicRecordRepository
        extends MongoRepository<Music, Long> {

    /**
     * {@inheritDoc}
     */
    List<Music> findByListenTimeBetween(Instant startDate,
                                        Instant endDate);
}
