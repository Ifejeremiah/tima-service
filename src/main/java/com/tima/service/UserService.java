package com.tima.service;

import com.tima.dao.UserDao;
import com.tima.enums.UserStatus;
import com.tima.exception.DuplicateEntityException;
import com.tima.exception.NotFoundException;
import com.tima.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService extends BaseService<User> {
    UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public long create(User user) {
        try {
            checkUserExists(user.getEmail());
            return userDao.create(user);
        } catch (Exception error) {
            log.error("Error creating user", error);
            throw error;
        }
    }

    private void checkUserExists(String email) {
        if (this.findByEmail(email) != null)
            throw new DuplicateEntityException("User with this email already exists");
    }

    public User findByEmail(String email) {
        try {
            return userDao.findByEmail(email);
        } catch (Exception error) {
            log.error("Error fetching user by email", error);
            throw error;
        }
    }

    public User findByEmail(String email, Boolean throwException) {
        try {
            User user = userDao.findByEmail(email);
            if (user == null) throw new NotFoundException("Could not find user with email " + email);
            return user;
        } catch (Exception error) {
            log.error("Error fetching user by email, throw exception", error);
            throw error;
        }
    }

    public User findById(int id) {
        try {
            User user = userDao.find(id);
            if (user == null) throw new NotFoundException("Could not find user with user id " + id);
            return user;
        } catch (Exception error) {
            log.error("Error fetching user", error);
            throw error;
        }
    }

    public void activateEmail(User user) {
        try {
            user.setEmailConfirmed(true);
        } catch (Exception error) {
            log.error("Error activating user email", error);
            throw error;
        }
    }

    public void delete(int id) {
        try {
            User existing = this.findById(id);
            existing.setUserStatus(UserStatus.DELETED);
//            updateById(existing.getId(), existing);
        } catch (Exception error) {
            log.error("Error deleting user", error);
            throw error;
        }
    }
}
