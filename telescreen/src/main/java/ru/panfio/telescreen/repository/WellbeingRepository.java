package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.Wellbeing;

import java.time.LocalDateTime;
import java.util.List;

public interface WellbeingRepository extends CrudRepository<Wellbeing, Long> {

    /**
     * {@inheritDoc}
     */
    List<Wellbeing> findByStartTimeBetween(LocalDateTime startDate,
                                           LocalDateTime endDate);

    /**
     * {@inheritDoc}
     */
    Wellbeing findByStartTimeAndEndTime(LocalDateTime startDate,
                                        LocalDateTime endDate);
}
