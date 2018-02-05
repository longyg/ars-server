package com.longyg.frontend.model.ne;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface NeParamRepository extends MongoRepository<NeParam, String> {
    @Query("{'neType': ?0, 'neVersion': ?1}")
    List<NeParam> findByNeRelease(String neType, String neRel);
}
