package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.YouTube;

import java.time.LocalDateTime;
import java.util.List;

public interface YouTubeRepository extends CrudRepository<YouTube, Long> {

    /**
     * {@inheritDoc}
     */
    List<YouTube> findByTimeBetween(LocalDateTime startTime,
                                    LocalDateTime endTime);

}
