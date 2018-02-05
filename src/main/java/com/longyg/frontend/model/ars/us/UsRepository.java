package com.longyg.frontend.model.ars.us;

import com.longyg.frontend.model.ne.NeRelease;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsRepository extends MongoRepository<UserStorySpec, String> {
    UserStorySpec findByNe(NeRelease ne);
    void deleteByNe(NeRelease ne);
}
