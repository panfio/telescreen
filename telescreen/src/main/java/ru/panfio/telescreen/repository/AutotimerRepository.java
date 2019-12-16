package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.Autotimer;

import java.time.LocalDateTime;
import java.util.List;

public interface AutotimerRepository extends CrudRepository<Autotimer, String> {
    @Query(value = "from Autotimer t where t.startTime BETWEEN :startTime AND :endTime")
    public List<Autotimer> getAllBetweenDates(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
