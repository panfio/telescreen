package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.Wellbeing;

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
