package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Wellbeing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.time.Instant;
import java.util.List;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "wellbeings", path = "wellbeing")
public interface WellbeingRepository extends MongoRepository<Wellbeing, Long> {

    /**
     * {@inheritDoc}
     */
    @RestResource
    List<Wellbeing> findByStartTimeBetween(@Param("startTime") Instant startTime,
                                           @Param("endTime") Instant endTime);

    /**
     * {@inheritDoc}
     */
    Wellbeing findByStartTimeAndEndTime(@Param("startTime") Instant startTime,
                                        @Param("endTime") Instant endTime);
}
