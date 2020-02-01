package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.TimeLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.time.Instant;
import java.util.List;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "timelogs", path = "timelog")
public interface TimeLogRepository extends MongoRepository<TimeLog, String> {

    /**
     * {@inheritDoc}
     */
    @RestResource
    List<TimeLog> findByStartDateBetween(@Param("startDate") Instant startDate,
                                         @Param("endDate") Instant endDate);
}
