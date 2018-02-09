package com.longyg.frontend.model.ne;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdaptationRepository extends MongoRepository<Adaptation, String> {
    void deleteByIdIn(List<String> ids);

    List<Adaptation> findByNeType(String neType);
}
