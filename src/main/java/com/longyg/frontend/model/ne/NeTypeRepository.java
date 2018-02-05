package com.longyg.frontend.model.ne;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface NeTypeRepository extends MongoRepository<NeType, String> {
    NeType findByName(String name);
    void deleteByIdIn(List<String> ids);
}
