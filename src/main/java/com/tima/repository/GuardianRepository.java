package com.tima.repository;

import com.tima.model.Guardian;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GuardianRepository extends MongoRepository<Guardian, String> {
}
