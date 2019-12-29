package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.Media;

import java.time.LocalDateTime;
import java.util.List;


public interface MediaRepository extends CrudRepository<Media, Long> {

    /**
     * {@inheritDoc}
     */
    List<Media> findByCreatedBetween(LocalDateTime startDate,
             LocalDateTime endDate);
}
