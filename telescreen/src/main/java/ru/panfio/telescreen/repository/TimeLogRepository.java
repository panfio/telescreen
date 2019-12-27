package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.TimeLog;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeLogRepository extends CrudRepository<TimeLog, String> {

    /**
     * Returns list of records from time period.
     *
     * @param startDate period start
     * @param endDate   period end
     * @return list
     */
    @Query(value = "from TimeLog t "
            + "where t.startDate BETWEEN :startDate AND :endDate")
    List<TimeLog> getAllBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
