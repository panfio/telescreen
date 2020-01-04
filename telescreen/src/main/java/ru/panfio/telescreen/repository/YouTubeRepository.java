package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.YouTube;

import java.time.LocalDateTime;
import java.util.List;

public interface YouTubeRepository extends MongoRepository<YouTube, Long> {

    /**
     * {@inheritDoc}
     */
    List<YouTube> findByTimeBetween(LocalDateTime startTime,
                                    LocalDateTime endTime);

}
