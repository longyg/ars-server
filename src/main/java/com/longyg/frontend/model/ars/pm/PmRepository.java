package com.longyg.frontend.model.ars.pm;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PmRepository extends MongoRepository<PmDataLoadSpec, String> {
}
