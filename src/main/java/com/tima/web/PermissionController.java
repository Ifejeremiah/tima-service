package com.tima.web;

import com.tima.dto.Response;
import com.tima.model.Permission;
import com.tima.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/permissions")
public class PermissionController {
    PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Permission> create(@Validated @RequestBody Permission request) {
        permissionService.create(request);
        return new Response<>("Permission created successfully");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<Permission>> findAll(
            @RequestParam(name = "search_query", required = false) String searchQuery) {
        Response<List<Permission>> response = new Response<>();
        response.setData(permissionService.findAll(searchQuery));
        response.setMessage("Permissions fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Permission> find(@PathVariable int id) {
        Response<Permission> response = new Response<>();
        response.setData(permissionService.findById(id));
        response.setMessage("Permission fetched successfully");
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Permission> update(@PathVariable int id, @RequestBody @Validated Permission updateRequest) {
        permissionService.update(id, updateRequest);
        return new Response<>("Permission updated successfully");
    }
}
