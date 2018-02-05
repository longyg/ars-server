package com.longyg.frontend.model.ars.om;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OmRepository extends MongoRepository<ObjectModelSpec, String> {
}
