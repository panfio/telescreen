package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.MiFitActivity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.time.Instant;
import java.util.List;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "mifits", path = "mifit")
public interface MiFitActivityRepository extends MongoRepository<MiFitActivity, String> {

    /**
     * {@inheritDoc}
     */
    @RestResource
    List<MiFitActivity> findByDateBetween(@Param("startDate") Instant startDate,
                                          @Param("endDate") Instant endDate);

    /**
     * {@inheritDoc}
     */
    MiFitActivity findByDate(@Param("date") Instant date);
}
