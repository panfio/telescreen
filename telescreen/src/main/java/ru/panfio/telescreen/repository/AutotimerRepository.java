package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.Autotimer;

import java.time.LocalDateTime;
import java.util.List;

public interface AutotimerRepository extends CrudRepository<Autotimer, String> {

    /**
     * {@inheritDoc}
     */
    List<Autotimer> findByStartTimeBetween(LocalDateTime startTime,
                                           LocalDateTime endTime);
}
