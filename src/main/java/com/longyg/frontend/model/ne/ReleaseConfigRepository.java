package com.longyg.frontend.model.ne;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReleaseConfigRepository extends MongoRepository<ReleaseConfig, String> {
    void deleteByIdIn(List<String> ids);
}
