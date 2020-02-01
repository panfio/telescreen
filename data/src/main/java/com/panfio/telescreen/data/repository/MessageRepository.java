package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.Instant;
import java.util.List;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "messages", path = "message")
public interface MessageRepository extends MongoRepository<Message, String> {

    /**
     * {@inheritDoc}
     */
    @RestResource
    List<Message> findByCreatedBetween(@Param("startDate") Instant startDate,
                                       @Param("endDate") Instant endDate);

    /**
     * {@inheritDoc}
     */
    Message findByLegacyIDAndCreated(@Param("legacyID") String legacyID,
                                     @Param("created") Instant created);
}
