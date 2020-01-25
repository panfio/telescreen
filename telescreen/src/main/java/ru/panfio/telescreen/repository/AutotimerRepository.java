package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.Autotimer;

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
