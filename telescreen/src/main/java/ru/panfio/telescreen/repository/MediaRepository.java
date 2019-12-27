package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.Media;

import java.time.LocalDateTime;
import java.util.List;


public interface MediaRepository extends CrudRepository<Media, Long> {

    /**
     * Returns list of records from time period.
     *
     * @param startDate period start
     * @param endDate   period end
     * @return list
     */
    @Query(value = "from Media t "
            + "where t.created BETWEEN :startDate AND :endDate")
    List<Media> getAllBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
