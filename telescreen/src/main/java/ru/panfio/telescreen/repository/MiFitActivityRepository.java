package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.MiFitActivity;

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
