package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.YouTube;

import java.time.Instant;
import java.util.List;

public interface YouTubeRepository extends MongoRepository<YouTube, Long> {

    /**
     * {@inheritDoc}
     */
    List<YouTube> findByTimeBetween(Instant startTime,
                                    Instant endTime);

}
