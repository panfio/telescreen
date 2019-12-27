package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    /**
     * Returns list of records from time period.
     *
     * @param startDate period start
     * @param endDate   period end
     * @return list
     */
    @Query(value = "from Message t "
            + "where t.created BETWEEN :startDate AND :endDate")
    List<Message> getAllBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Find by legacyId and created time.
     *
     * @param legacyId message external id
     * @param created  time
     * @return record
     */
    @Query(value = "select m from Message m "
            + "where m.legacyID = :legacyId and m.created = :created")
    Message findByLegacyIdAndCreatedTime(
            @Param("legacyId") String legacyId,
            @Param("created") LocalDateTime created);
}
