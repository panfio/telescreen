package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.Wellbeing;

import java.time.LocalDateTime;
import java.util.List;

public interface WellbeingRepository extends CrudRepository<Wellbeing, Long> {

    /**
     * {@inheritDoc}
     */
    List<Wellbeing> findByStartTimeBetween(LocalDateTime startTime,
                                           LocalDateTime endTime);

    /**
     * {@inheritDoc}
     */
    Wellbeing findByStartTimeAndEndTime(LocalDateTime startTime,
                                        LocalDateTime endTime);
}
