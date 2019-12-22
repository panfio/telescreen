package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.Wellbeing;

import java.time.LocalDateTime;
import java.util.List;

public interface WellbeingRepository extends CrudRepository<Wellbeing,Long> {
    @Query(value = "from Wellbeing t where t.startTime BETWEEN :startDate AND :endDate")
    public List<Wellbeing> getAllBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = "from Wellbeing t where t.startTime = :startDate AND t.endTime = :endDate")
    public Wellbeing findWellbeingRecord(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
