package com.tima.service;

import com.tima.dao.AdminUserDao;
import com.tima.dto.AdminUserUpdateRequest;
import com.tima.exception.NotFoundException;
import com.tima.model.AdminUser;
import com.tima.model.Mail;
import com.tima.model.Page;
import com.tima.util.AuthUtil;
import com.tima.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminUserService {
    AdminUserDao adminUserDao;
    UserService userService;
    MailService mailService;
    OTPService otpService;

    public AdminUserService(AdminUserDao adminUserDao, UserService userService, MailService mailService, OTPService otpService) {
        this.adminUserDao = adminUserDao;
        this.userService = userService;
        this.mailService = mailService;
        this.otpService = otpService;
    }

    public long create(AdminUser adminUser) {
        try {
            userService.checkUserExists(adminUser.getEmail());
            adminUser.setCreatedBy(AuthUtil.getCurrentUserEmail());
            String otp = otpService.create(adminUser.getEmail());
            mailService.sendMail(adminUser.getEmail(), constructMail());
            mailService.sendMail(adminUser.getEmail(), MailUtil.constructOTPMail(otp));
            return adminUserDao.create(adminUser);
        } catch (Exception error) {
            log.error("Error creating admin user", error);
            throw error;
        }
    }

    private Mail constructMail() {
        Mail mail = new Mail();
        mail.setSubject("You have been created as an admin user");
        mail.setContext("Hello! <br\\>%s</b>. You have been created as an admin user.");
        return mail;
    }

    public Page<AdminUser> findAll(int page, int size, String searchQuery) {
        try {
            return adminUserDao.findAll(page, size, searchQuery);
        } catch (Exception error) {
            log.error("Error fetching all admin users", error);
            throw error;
        }
    }

    public AdminUser findById(int id) {
        try {
            AdminUser adminUser = adminUserDao.find(id);
            if (adminUser == null) throw new NotFoundException("Could not find admin user with id " + id);
            return adminUser;
        } catch (Exception error) {
            log.error("Error fetching admin user", error);
            throw error;
        }
    }

    public void update(int id, AdminUserUpdateRequest request) {
        try {
            AdminUser existing = findById(id);
            AdminUser adminUser = new AdminUser();
            BeanUtils.copyProperties(request, adminUser);
            adminUser.setId(existing.getId());
            adminUser.setLastUpdatedBy(AuthUtil.getCurrentUserEmail());
            adminUserDao.update(adminUser);
        } catch (Exception error) {
            log.error("Error updating admin user", error);
            throw error;
        }
    }
}
