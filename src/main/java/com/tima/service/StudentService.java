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
    UserService userService;
    StudentDao studentDao;

    public StudentService(UserService userService, StudentDao studentDao) {
        this.userService = userService;
        this.studentDao = studentDao;
    }

    public void create(Student student) {
        try {
            checkUserExists(fetchCurrentUserId());
            student.setUserId(fetchCurrentUserId());
            studentDao.create(student);
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
                throw new NotFoundException("Could not find student with user id " + fetchCurrentUserId());
            return student;
        } catch (Exception error) {
            log.error("Error fetching student by current user id", error);
            throw error;
        }
    }

    public void update(int id, Student update) {
        try {
            Student existing = this.findById(id);
            update.setId(existing.getId());
            studentDao.update(update);
        } catch (Exception error) {
            log.error("Error updating student", error);
            throw error;
        }
    }

    public void delete(int id) {
        try {
            Student student = this.findById(id);
            userService.delete(student.getUserId());
        } catch (Exception error) {
            log.error("Error deleting student", error);
            throw error;
        }
    }
}
