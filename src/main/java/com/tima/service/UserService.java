package com.tima.service;

import com.tima.exception.NotFoundException;
import com.tima.model.User;
import com.tima.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception error) {
            log.error("Error creating user", error);
            throw error;
        }
    }

    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format("Could not find user with email: %s", email)));
        } catch (Exception error) {
            log.error("Error fetching user by email", error);
            throw error;
        }
    }

    public Boolean existsByEmail(String email) {
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception error) {
            log.error("Error checking email exists", error);
            throw error;
        }
    }

    public void activateEmail(User user) {
        try {
            user.setEmailConfirmed(true);
            userRepository.save(user);
        } catch (Exception error) {
            log.error("Error activating user email", error);
            throw error;
        }
    }
}
