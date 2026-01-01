package br.com.emanueldias.Log.repository;

import br.com.emanueldias.Log.model.LogMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<LogMessageEntity, String> {
}
