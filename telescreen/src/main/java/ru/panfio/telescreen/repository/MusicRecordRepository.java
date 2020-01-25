package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.Music;

import java.time.Instant;
import java.util.List;

public interface MusicRecordRepository
        extends MongoRepository<Music, Long> {

    /**
     * {@inheritDoc}
     */
    List<Music> findByListenTimeBetween(Instant startDate,
                                        Instant endDate);
}
