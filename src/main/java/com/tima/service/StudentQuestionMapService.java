package com.tima.service;

import com.tima.model.StudentQuestionMap;
import com.tima.repository.StudentQuestionMapRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentQuestionMapService {
    StudentQuestionMapRepository studentQuestionMapRepository;

    public StudentQuestionMapService(StudentQuestionMapRepository studentQuestionMapRepository) {
        this.studentQuestionMapRepository = studentQuestionMapRepository;
    }

    public void create(StudentQuestionMap map) {
        try {
            studentQuestionMapRepository.save(map);
        } catch (Exception error) {
            log.error("Error creating student question map", error);
            throw error;
        }
    }
}
