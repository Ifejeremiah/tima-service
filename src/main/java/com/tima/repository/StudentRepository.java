package com.tima.repository;

import com.tima.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    @Query("{userId:'?0'}")
    Optional<Student> findByUserId(String userId);

    Boolean existsByUserId(String userId);
}
