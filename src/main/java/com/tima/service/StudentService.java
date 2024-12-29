package com.tima.service;

import com.tima.dao.StudentDao;
import com.tima.exception.DuplicateEntityException;
import com.tima.exception.NotFoundException;
import com.tima.model.Page;
import com.tima.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentService extends BaseService {
    StudentDao studentDao;
    UserService userService;

    public StudentService(StudentDao studentDao, UserService userService) {
        this.studentDao = studentDao;
        this.userService = userService;
    }

    public void create(Student student) {
        try {
            checkUserExists(fetchCurrentUserId());
            student.setUserId(fetchCurrentUserId());
            studentDao.create(student);
            userService.activateUser(fetchCurrentUserId());
        } catch (Exception error) {
            log.error("Error creating student", error);
            throw error;
        }
    }

    private void checkUserExists(int userId) {
        if (studentDao.findByUserId(userId) != null)
            throw new DuplicateEntityException("Student with this user id already exists");
    }

    public Page<Student> findAll(int page, int size, String searchQuery) {
        try {
            return studentDao.findAll(page, size, searchQuery);
        } catch (Exception error) {
            log.error("Error fetching all students", error);
            throw error;
        }
    }

    public Student findById(int id) {
        try {
            Student student = studentDao.find(id);
            if (student == null) throw new NotFoundException("Could not find student with student id " + id);
            return student;
        } catch (Exception error) {
            log.error("Error fetching student", error);
            throw error;
        }
    }

    public Student findByUserId() {
        try {
            Student student = studentDao.findByUserId(fetchCurrentUserId());
            if (student == null)
                throw new NotFoundException("No student found with your id");
            return student;
        } catch (Exception error) {
            log.error("Error fetching student by current user id", error);
            throw error;
        }
    }

    public void update(Student update) {
        try {
            Student existing = this.findByUserId();
            update.setId(existing.getId());
            studentDao.update(update);
        } catch (Exception error) {
            log.error("Error updating student by current user id", error);
            throw error;
        }
    }
}
