package com.longyg.frontend.model.config;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlarmObjectRepository extends MongoRepository<AlarmObject, String> {
    void deleteByIdIn(List<String> ids);
}
