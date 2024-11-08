package com.tima.service;

import com.tima.exception.NotFoundException;
import com.tima.model.Student;
import com.tima.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StudentService {
    StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void create(Student student) {
        try {
            studentRepository.save(student);
        } catch (Exception error) {
            log.error("Error creating student", error);
            throw error;
        }
    }

    public List<Student> findAll(int page, int size) {
        try {
            return studentRepository.findAll();
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

    public void update(String id, Student updateStudentRequest) {
        try {
            Student student = this.findById(id);
            BeanUtils.copyProperties(updateStudentRequest, student);
            studentRepository.save(student);
        } catch (Exception error) {
            log.error("Error updating student", error);
            throw error;
        }
    }
}
