package com.longyg.frontend.model.ars.counter;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CounterRepository extends MongoRepository<CounterSpec, String> {
}
