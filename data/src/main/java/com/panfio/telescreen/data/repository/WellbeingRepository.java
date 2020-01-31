package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Wellbeing;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.time.Instant;
import java.util.List;

public interface WellbeingRepository extends MongoRepository<Wellbeing, Long> {

    /**
     * {@inheritDoc}
     */
    List<Wellbeing> findByStartTimeBetween(Instant startTime,
                                           Instant endTime);

    /**
     * {@inheritDoc}
     */
    Wellbeing findByStartTimeAndEndTime(Instant startTime,
                                        Instant endTime);
}
