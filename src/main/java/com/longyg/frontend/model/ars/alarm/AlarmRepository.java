package com.longyg.frontend.model.ars.alarm;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlarmRepository extends MongoRepository<AlarmSpec, String> {
}
