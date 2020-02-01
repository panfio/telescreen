package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Media;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.Instant;
import java.util.List;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "medias", path = "media")
public interface MediaRepository extends MongoRepository<Media, String> {

    /**
     * {@inheritDoc}
     */
    @RestResource
    List<Media> findByCreatedBetween(@Param("startDate") Instant startDate,
                                     @Param("endDate") Instant endDate);
}
