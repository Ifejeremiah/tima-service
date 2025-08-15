package com.tima.service;

import com.tima.dao.RoleDao;
import com.tima.exception.DuplicateEntityException;
import com.tima.exception.NotFoundException;
import com.tima.model.Page;
import com.tima.model.Permission;
import com.tima.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoleService {
    RoleDao roleDao;
    PermissionService permissionService;

    public RoleService(RoleDao roleDao, PermissionService permissionService) {
        this.roleDao = roleDao;
        this.permissionService = permissionService;
    }

    public void create(Role role) {
        try {
            if (roleDao.findByName(role.getName()) != null)
                throw new DuplicateEntityException("Role with this name already exists");
            roleDao.create(role);
        } catch (Exception error) {
            log.error("Error creating role", error);
            throw error;
        }
    }

    public Page<Role> findAll(int page, int size, String searchQuery) {
        try {
            return roleDao.findAll(page, size, searchQuery);
        } catch (Exception error) {
            log.error("Error fetching all roles", error);
            throw error;
        }
    }

    public Role findById(int id) {
        try {
            Role role = roleDao.find(id);
            if (role == null) throw new NotFoundException("Could not find role with role id " + id);
            return role;
        } catch (Exception error) {
            log.error("Error fetching role", error);
            throw error;
        }
    }

    public void update(int id, Role role) {
        try {
            Role existing = findById(id);
            if (!existing.getName().equalsIgnoreCase(role.getName()) && roleDao.findByName(role.getName()) != null)
                throw new DuplicateEntityException("Role with this name already exists");
            role.setId(existing.getId());
            roleDao.update(role);
        } catch (Exception error) {
            log.error("Error updating role", error);
            throw error;
        }
    }

    public void assignPermissionToRole(int roleId, int permissionId) {
        try {
            Role role = findById(roleId);
            Permission permission = permissionService.findById(permissionId);
            Permission existing = roleDao.findRolePermission(role.getId(), permission.getId());
            if (existing != null)
                throw new DuplicateEntityException("Permission with ID " + permissionId + " is already assigned to role with ID " + roleId);
            roleDao.createRolePermission(role.getId(), permission.getId());
        } catch (Exception error) {
            log.error("Error assigning permission to role", error);
            throw error;
        }
    }

    public void removePermissionOnRole(int roleId, int permissionId) {
        try {
            Role role = findById(roleId);
            Permission permission = permissionService.findById(permissionId);
            Permission existing = roleDao.findRolePermission(role.getId(), permission.getId());
            if (existing == null)
                throw new NotFoundException("Permission with ID " + permissionId + " is NOT assigned to role with ID " + roleId);
            roleDao.deleteRolePermission(role.getId(), permission.getId());
        } catch (Exception error) {
            log.error("Error removing permission on role", error);
            throw error;
        }
    }

    public List<Permission> findPermissionsOnRole(int roleId) {
        try {
            Role role = findById(roleId);
            return roleDao.findPermissionsOnRole(role.getId());
        } catch (Exception error) {
            log.error("Error fetching permissions on role", error);
            throw error;
        }
    }
}
