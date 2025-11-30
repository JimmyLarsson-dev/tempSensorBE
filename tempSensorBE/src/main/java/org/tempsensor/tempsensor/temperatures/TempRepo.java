package org.tempsensor.tempsensor.temperatures;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TempRepo extends MongoRepository<TempDoc, String> {
}
