package com.longyg.frontend.model.ne;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface NeInterfaceRepository extends MongoRepository<NeInterface, String> {
    @Query("{'neType': ?0, 'neVersion': ?1}")
    NeInterface findByNeRelease(String neType, String neVersion);
}
