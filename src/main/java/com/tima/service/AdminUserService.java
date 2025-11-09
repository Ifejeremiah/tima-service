package com.tima.service;

import com.tima.dao.AdminUserDao;
import com.tima.dto.AdminUserUpdateRequest;
import com.tima.exception.DuplicateEntityException;
import com.tima.exception.NotFoundException;
import com.tima.model.AdminUser;
import com.tima.model.Mail;
import com.tima.model.Page;
import com.tima.model.Role;
import com.tima.util.AuthUtil;
import com.tima.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AdminUserService {
    AdminUserDao adminUserDao;
    UserService userService;
    MailService mailService;
    OTPService otpService;
    RoleService roleService;

    public AdminUserService(AdminUserDao adminUserDao, UserService userService, MailService mailService, OTPService otpService, RoleService roleService) {
        this.adminUserDao = adminUserDao;
        this.userService = userService;
        this.mailService = mailService;
        this.otpService = otpService;
        this.roleService = roleService;
    }

    public AdminUser create(AdminUser adminUser) {
        try {
            userService.checkUserExists(adminUser.getEmail());
            adminUser.setCreatedBy(AuthUtil.getCurrentUserEmail());
            String otp = otpService.create(adminUser.getEmail());
            mailService.sendMail(adminUser.getEmail(), constructMail());
            mailService.sendMail(adminUser.getEmail(), MailUtil.constructOTPMail(otp));
            long adminId = adminUserDao.create(adminUser);
            adminUser.setId((int) adminId);
            return adminUser;
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
            adminUser.setRoles(adminUserDao.findRolesOnUser(adminUser.getId()));
            adminUser.setPermissions(adminUserDao.findPermissionsOnUser(adminUser.getId()));
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

    public void assignRoleToUser(int userId, int roleId) {
        try {
            AdminUser adminUser = findById(userId);
            Role role = roleService.findById(roleId);
            Role existing = adminUserDao.findUserRole(adminUser.getId(), role.getId());
            if (existing != null)
                throw new DuplicateEntityException("Role with ID " + roleId + " is already assigned to user with ID " + userId);
            adminUserDao.createUserRole(adminUser.getId(), role.getId());
        } catch (Exception error) {
            log.error("Error assigning role to user", error);
            throw error;
        }
    }

    public void removeRoleOnUser(int userId, int roleId) {
        try {
            AdminUser adminUser = findById(userId);
            Role role = roleService.findById(roleId);
            Role existing = adminUserDao.findUserRole(adminUser.getId(), role.getId());
            if (existing == null)
                throw new NotFoundException("Role with ID " + roleId + " is NOT assigned to user with ID " + userId);
            adminUserDao.deleteUserRole(adminUser.getId(), role.getId());
        } catch (Exception error) {
            log.error("Error removing role on user", error);
            throw error;
        }
    }

    public List<Role> findRolesOnUser(int userId) {
        try {
            AdminUser adminUser = findById(userId);
            return adminUserDao.findRolesOnUser(adminUser.getId());
        } catch (Exception error) {
            log.error("Error fetching roles on user", error);
            throw error;
        }
    }
}
