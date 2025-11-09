package com.tima.web;

import com.tima.dto.AdminUserUpdateRequest;
import com.tima.dto.Response;
import com.tima.model.AdminUser;
import com.tima.model.Page;
import com.tima.model.Role;
import com.tima.service.AdminUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin/users")
public class AdminUserController {
    AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<AdminUser> create(@Validated @RequestBody AdminUser request) {
        Response<AdminUser> response = new Response<>();
        response.setData(adminUserService.create(request));
        response.setMessage("Admin user created successfully");
        return response;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<AdminUser>> findAll(
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size,
            @RequestParam(name = "search_query", required = false) String searchQuery) {
        Response<Page<AdminUser>> response = new Response<>();
        response.setData(adminUserService.findAll(page, size, searchQuery));
        response.setMessage("Admin users fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<AdminUser> find(@PathVariable int id) {
        Response<AdminUser> response = new Response<>();
        response.setData(adminUserService.findById(id));
        response.setMessage("Admin user fetched successfully");
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<AdminUser> update(@PathVariable int id, @RequestBody @Validated AdminUserUpdateRequest updateRequest) {
        adminUserService.update(id, updateRequest);
        return new Response<>("Admin user updated successfully");
    }

    @PostMapping(value = "/{userId}/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<AdminUser> assignRoleToUser(@PathVariable Integer userId, @PathVariable Integer roleId) {
        adminUserService.assignRoleToUser(userId, roleId);
        return new Response<>("Role with ID " + roleId + " is assigned to admin user with ID " + userId + " successfully");
    }

    @DeleteMapping(value = "/{userId}/roles/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<AdminUser> removePermissionOnRole(@PathVariable Integer userId, @PathVariable Integer roleId) {
        adminUserService.removeRoleOnUser(userId, roleId);
        return new Response<>("Role with ID " + roleId + " is removed on admin user with ID " + userId + " successfully");
    }

    @GetMapping(value = "/{userId}/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<Role>> findPermissionsOnRole(@PathVariable int userId) {
        Response<List<Role>> response = new Response<>();
        response.setData(adminUserService.findRolesOnUser(userId));
        response.setMessage("Roles on userId " + userId + " fetched successfully");
        return response;
    }
}
