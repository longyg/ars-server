package com.longyg.frontend.model.ne;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReleaseConfigRepository extends MongoRepository<ReleaseConfig, String> {
    void deleteByIdIn(List<String> ids);

    @Query("{'neType': ?0, 'neVersion': ?1}")
    ReleaseConfig findByNeTypeAndVersion(String neType, String neVersion);
}
