package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Autotimer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.Instant;
import java.util.List;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "autotimers", path = "autotimer")
public interface AutotimerRepository extends MongoRepository<Autotimer, String> {

    /**
     * {@inheritDoc}
     */
    @RestResource
    List<Autotimer> findByStartTimeBetween(@Param("startTime") Instant startTime,
                                           @Param("endTime") Instant endTime);
}
