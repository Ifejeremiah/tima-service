package com.tima.repository;

import com.tima.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
    //    @Aggregation(pipeline = {"{ '$skip' : ?1 }", "{ '$limit' : ?2 }"})
//    Page<Student> findStudents(Pageable pageable);
}
