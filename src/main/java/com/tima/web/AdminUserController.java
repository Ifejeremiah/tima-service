package com.tima.web;

import com.tima.dto.AdminUserUpdateRequest;
import com.tima.dto.Response;
import com.tima.model.AdminUser;
import com.tima.model.Page;
import com.tima.service.AdminUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/admin-users")
public class AdminUserController {
    AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<AdminUser> create(@Validated @RequestBody AdminUser request) {
        adminUserService.create(request);
        return new Response<>("Admin user created successfully");
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
}
