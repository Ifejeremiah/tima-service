package com.tima.repository;

import com.tima.enums.QuestionBankStatus;
import com.tima.model.QuestionBank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuestionBankRepository extends MongoRepository<QuestionBank, String> {
    @Query(fields = "{}")
    List<QuestionBank> findAllBySubjectAndTopicAndDifficulty(String subject, String topic, QuestionBankStatus difficulty, int limit);
}


"id": "67420ce6e5b5b07afd6a7a47",
        "createdOn": "2024-11-23T17:12:06.954Z",
        "lastUpdatedOn": "2024-11-23T17:12:06.954Z",
        "subject": "Mathematics",
        "topic": "Geometry",
        "difficulty": "MEDIUM",
        "question": "what is the sum of 2 + 2?",
        "options": [
        "1",
        "2",
        "3",
        "4"
        ],
        "answer": 1