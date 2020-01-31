package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.MiFitActivity;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.time.Instant;
import java.util.List;

public interface MiFitActivityRepository
        extends MongoRepository<MiFitActivity, String> {

    /**
     * {@inheritDoc}
     */
    List<MiFitActivity> findByDateBetween(Instant startDate,
                                          Instant endDate);

    /**
     * {@inheritDoc}
     */
    MiFitActivity findByDate(Instant date);
}
