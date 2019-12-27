package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.Autotimer;

import java.time.LocalDateTime;
import java.util.List;

public interface AutotimerRepository extends CrudRepository<Autotimer, String> {

    /**
     * Returns list of records from time period.
     *
     * @param startTime period start
     * @param endTime   period end
     * @return list
     */
    @Query(value = "from Autotimer t "
            + "where t.startTime BETWEEN :startTime AND :endTime")
    List<Autotimer> getAllBetweenDates(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
