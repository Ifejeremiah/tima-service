package com.tima.service;

import com.tima.dao.UserDao;
import com.tima.dto.ChangePasswordRequest;
import com.tima.enums.UserStatus;
import com.tima.exception.BadRequestException;
import com.tima.exception.DuplicateEntityException;
import com.tima.exception.NotFoundException;
import com.tima.model.Mail;
import com.tima.model.Page;
import com.tima.model.User;
import com.tima.util.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService extends BaseService {
    UserDao userDao;
    Encoder encoder;
    MailService mailService;

    public UserService(UserDao userDao, Encoder encoder, MailService mailService) {
        this.userDao = userDao;
        this.encoder = encoder;
        this.mailService = mailService;
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

    public Page<User> findAll(int page, int size, String searchQuery) {
        try {
            return userDao.findAll(page, size, searchQuery);
        } catch (Exception error) {
            log.error("Error fetching all users", error);
            throw error;
        }
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
            if (user == null && throwException) throw new NotFoundException("Could not find user with email " + email);
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

    public User findByCurrentUser() {
        try {
            return this.findById(fetchCurrentUserId());
        } catch (Exception error) {
            log.error("Error fetching current user", error);
            throw error;
        }
    }

    public void updateLastLogin(int id) {
        try {
            userDao.setLastLogin(id);
        } catch (Exception error) {
            log.error("Error updating user last login", error);
            throw error;
        }
    }

    public void activateUser(int userId) {
        try {
            User user = this.findById(userId);
            user.setStatus(UserStatus.ACTIVE);
            userDao.update(user);
        } catch (Exception error) {
            log.error("Error activating user", error);
            throw error;
        }
    }

    public void activateEmail(String email) {
        try {
            User user = this.findByEmail(email);
            user.setEmailConfirmed(true);
            userDao.update(user);
        } catch (Exception error) {
            log.error("Error activating user email", error);
            throw error;
        }
    }

    public void changePassword(ChangePasswordRequest passwordRequest) {
        try {
            User user = this.findByEmail(fetchCurrentUserEmail());
            if (!encoder.validatePassword(passwordRequest.getOldPassword(), user.getPassword()))
                throw new BadRequestException("Old password is incorrect");
            if (encoder.validatePassword(passwordRequest.getNewPassword(), user.getPassword()))
                throw new BadRequestException("New password is same as old password");
            userDao.updatePassword(fetchCurrentUserId(), encoder.encodePassword(passwordRequest.getNewPassword()));
            mailService.sendMail(user.getEmail(), composeMail());
        } catch (Exception error) {
            log.error("Error changing user password", error);
            throw error;
        }
    }

    private Mail composeMail() {
        Mail mail = new Mail();
        mail.setSubject("Password change update");
        mail.setContext("This is to notify you that your password just got changed.");
        return mail;
    }

    public void deleteByCurrentUser() {
        try {
            User existing = this.findById(fetchCurrentUserId());
            userDao.delete(existing.getId());
        } catch (Exception error) {
            log.error("Error deleting user by current user", error);
            throw error;
        }
    }

    public void delete(int id) {
        try {
            User existing = this.findById(id);
            userDao.delete(existing.getId());
        } catch (Exception error) {
            log.error("Error deleting user", error);
            throw error;
        }
    }
}
