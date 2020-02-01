package com.panfio.telescreen.data.repository;

import com.panfio.telescreen.model.Call;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.Instant;
import java.util.List;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "calls", path = "call")
public interface CallRecordRepository extends MongoRepository<Call, String> {

    /**
     * {@inheritDoc}
     */
    @RestResource
    List<Call> findByDateBetween(@Param("startDate") Instant startDate,
                                 @Param("endDate") Instant endDate);
}
