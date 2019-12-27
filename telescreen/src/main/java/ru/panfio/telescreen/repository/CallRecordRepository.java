package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.CallRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface CallRecordRepository extends CrudRepository<CallRecord, Long> {

    /**
     * Returns list of records from time period.
     *
     * @param startDate period start
     * @param endDate   period end
     * @return list
     */
    @Query(value = "from CallRecord t "
            + "where t.date BETWEEN :startDate AND :endDate")
    List<CallRecord> getAllBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
