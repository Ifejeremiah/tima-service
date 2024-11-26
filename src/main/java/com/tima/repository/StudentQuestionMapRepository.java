package com.tima.repository;

import com.tima.model.StudentQuestionMap;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentQuestionMapRepository extends MongoRepository<StudentQuestionMap, String> {
}
