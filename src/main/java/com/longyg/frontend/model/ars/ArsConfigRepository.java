package com.longyg.frontend.model.ars;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ArsConfigRepository extends MongoRepository<ArsConfig, String> {
    @Query("{'neType': ?0, 'neVersion':?1}")
    ArsConfig findByNeTypeAndRelease(String neType, String neVersion);
}
