package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.Media;

import java.time.LocalDateTime;
import java.util.List;


public interface MediaRepository extends CrudRepository<Media, Long> {
    @Query(value = "from Media t where t.created BETWEEN :startDate AND :endDate")
    public List<Media> getAllBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
