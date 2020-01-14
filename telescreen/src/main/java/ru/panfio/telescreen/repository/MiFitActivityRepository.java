package ru.panfio.telescreen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.panfio.telescreen.model.MiFitActivity;

import java.time.LocalDateTime;
import java.util.List;

public interface MiFitActivityRepository
        extends MongoRepository<MiFitActivity, String> {

    /**
     * {@inheritDoc}
     */
    List<MiFitActivity> findByDateBetween(LocalDateTime startDate,
                                          LocalDateTime endDate);

    /**
     * {@inheritDoc}
     */
    MiFitActivity findByDate(LocalDateTime date);
}
