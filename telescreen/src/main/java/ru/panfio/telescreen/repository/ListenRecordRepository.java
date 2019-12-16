package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.ListenRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface ListenRecordRepository extends CrudRepository<ListenRecord, Long> {
    @Query(value = "from ListenRecord t where t.listenTime BETWEEN :startDate AND :endDate")
    public List<ListenRecord> getAllBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
