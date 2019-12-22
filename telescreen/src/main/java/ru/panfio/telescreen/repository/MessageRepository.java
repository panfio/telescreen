package ru.panfio.telescreen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.panfio.telescreen.model.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    @Query(value = "from Message t where t.created BETWEEN :startDate AND :endDate")
    public List<Message> getAllBetweenDates(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    @Query(value = "select m from Message m where m.legacyID = :legacyId and m.created = :created")
    Message findByLegacyIdAndCreatedTime(@Param("legacyId") String legacyId,
                                         @Param("created") LocalDateTime created);
}
