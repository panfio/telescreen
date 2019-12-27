package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.YouTube;

import java.time.LocalDateTime;
import java.util.List;

public interface YouTubeRepository extends CrudRepository<YouTube, Long> {

    /**
     * Returns list of records from time period.
     *
     * @param startTime period start
     * @param endTime   period end
     * @return list
     */
    @Query(value = "from YouTube t "
            + "where t.time BETWEEN :startTime AND :endTime")
    List<YouTube> getAllBetweenDates(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

}
