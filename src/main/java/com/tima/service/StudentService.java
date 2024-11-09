package com.tima.service;

import com.tima.exception.NotFoundException;
import com.tima.model.Student;
import com.tima.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentService extends BaseService<Student> {
    StudentRepository studentRepository;
    MongoTemplate mongoTemplate;
    UserService userService;

    public StudentService(StudentRepository studentRepository, MongoTemplate mongoTemplate, UserService userService) {
        this.studentRepository = studentRepository;
        this.mongoTemplate = mongoTemplate;
        this.userService = userService;
    }

    public void create(Student student) {
        try {
            studentRepository.save(student);
        } catch (Exception error) {
            log.error("Error creating student", error);
            throw error;
        }
    }

    public Page<Student> findAll(int page, int size) {
        try {
            return studentRepository.findAll(PageRequest.of(page, size));
        } catch (Exception error) {
            log.error("Error fetching all students", error);
            throw error;
        }
    }

    public Student findById(String studentId) {
        try {
            return studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException(String.format("Could not find student with student id: %s", studentId)));
        } catch (Exception error) {
            log.error("Error fetching student");
            throw error;
        }
    }

    public void update(String id, Student update) {
        try {
            Student existing = this.findById(id);
            updateById(existing.getId(), update);
        } catch (Exception error) {
            log.error("Error updating student", error);
            throw error;
        }
    }

    public void delete(String id) {
        try {
//            Student student = this.findById(id);
            userService.delete(id);
//            studentRepository.deleteById(student.getId());
        } catch (Exception error) {
            log.error("Error deleting student", error);
            throw error;
        }
    }
}
