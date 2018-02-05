package com.longyg.frontend.model.config;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ObjectRepository extends MongoRepository<ParentObject, String> {
    void deleteByIdIn(List<String> ids);
}
