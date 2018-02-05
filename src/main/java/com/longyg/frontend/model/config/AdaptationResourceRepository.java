package com.longyg.frontend.model.config;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdaptationResourceRepository extends MongoRepository<AdaptationResource, String> {
    AdaptationResource findByAdaptation(Adaptation adaptation);
}
