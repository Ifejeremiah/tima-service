package com.tima.service;

import com.tima.dao.PermissionDao;
import com.tima.exception.DuplicateEntityException;
import com.tima.exception.NotFoundException;
import com.tima.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PermissionService {
    PermissionDao permissionDao;

    public PermissionService(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    public void create(Permission permission) {
        try {
            if (permissionDao.findByCode(permission.getCode()) != null)
                throw new DuplicateEntityException("Permission with this code already exists");
            permissionDao.create(permission);
        } catch (Exception error) {
            log.error("Error creating permission", error);
            throw error;
        }
    }

    public List<Permission> findAll(String searchQuery) {
        try {
            return permissionDao.findAll(searchQuery);
        } catch (Exception error) {
            log.error("Error fetching all permissions", error);
            throw error;
        }
    }

    public Permission findById(int id) {
        try {
            Permission permission = permissionDao.find(id);
            if (permission == null) throw new NotFoundException("Could not find permission with permission id " + id);
            return permission;
        } catch (Exception error) {
            log.error("Error fetching permission", error);
            throw error;
        }
    }

    public void update(int id, Permission permission) {
        try {
            Permission existing = findById(id);
            if (!existing.getCode().equalsIgnoreCase(permission.getCode()) && permissionDao.findByCode(permission.getCode()) != null)
                throw new DuplicateEntityException("Permission with this code already exists");
            permission.setId(existing.getId());
            permissionDao.update(permission);
        } catch (Exception error) {
            log.error("Error updating permission", error);
            throw error;
        }
    }
}
