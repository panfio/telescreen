package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.Autotimer;

import java.time.LocalDateTime;
import java.util.List;

public interface AutotimerRepository extends MongoRepository<Autotimer, String> {

    /**
     * {@inheritDoc}
     */
    List<Autotimer> findByStartTimeBetween(LocalDateTime startTime,
                                           LocalDateTime endTime);
}
