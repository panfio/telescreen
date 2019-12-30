package ru.panfio.telescreen.repository;

import org.springframework.data.repository.CrudRepository;
import ru.panfio.telescreen.model.Music;

import java.time.LocalDateTime;
import java.util.List;

public interface ListenRecordRepository
        extends CrudRepository<Music, Long> {

    /**
     * {@inheritDoc}
     */
    List<Music> findByListenTimeBetween(LocalDateTime startDate,
                                        LocalDateTime endDate);
}
