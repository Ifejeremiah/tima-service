package com.tima.service;

import com.tima.dao.RoleDao;
import com.tima.exception.DuplicateEntityException;
import com.tima.exception.NotFoundException;
import com.tima.model.Page;
import com.tima.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleService {
    RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
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
}
