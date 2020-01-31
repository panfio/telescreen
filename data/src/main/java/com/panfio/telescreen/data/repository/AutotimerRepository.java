package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Autotimer;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.time.Instant;
import java.util.List;

public interface AutotimerRepository
        extends MongoRepository<Autotimer, String> {

    /**
     * {@inheritDoc}
     */
    List<Autotimer> findByStartTimeBetween(Instant startTime,
                                           Instant endTime);
}
