package com.longyg.frontend.model.ars;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ArsRepository extends MongoRepository<ARS, String> {
    @Query("{'neType': ?0, 'neVersion':?1}")
    ARS findByNeTypeAndRelease(String neType, String neVersion);

    void deleteByIdIn(List<String> ids);
}
