package com.tima.repository;

import com.tima.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Query(value = "{email:'?0'}", fields = "{'firstName': 1, 'lastName': 1, 'email': 1, 'status': 1, 'emailConfirmed':  1}")
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
