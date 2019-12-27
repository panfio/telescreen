package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.Wellbeing;

import java.time.LocalDateTime;
import java.util.List;

public interface WellbeingRepository extends CrudRepository<Wellbeing, Long> {

    /**
     * Returns list of records from time period.
     *
     * @param startDate period start
     * @param endDate   period end
     * @return list
     */
    @Query(value = "from Wellbeing t "
            + "where t.startTime BETWEEN :startDate AND :endDate")
    List<Wellbeing> getAllBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Find by startDate and endDate.
     *
     * @param startDate start
     * @param endDate   end
     * @return Wellbeing Record
     */
    @Query(value = "from Wellbeing t "
            + "where t.startTime = :startDate AND t.endTime = :endDate")
    Wellbeing findWellbeingRecord(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
