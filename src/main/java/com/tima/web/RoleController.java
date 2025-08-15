package com.tima.web;

import com.tima.dto.Response;
import com.tima.model.Page;
import com.tima.model.Role;
import com.tima.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/roles")
public class RoleController {
    RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Role> create(@Validated @RequestBody Role request) {
        roleService.create(request);
        return new Response<>("Role created successfully");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<Role>> findAll(
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size,
            @RequestParam(name = "search_query", required = false) String searchQuery) {
        Response<Page<Role>> response = new Response<>();
        response.setData(roleService.findAll(page, size, searchQuery));
        response.setMessage("Roles fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Role> find(@PathVariable int id) {
        Response<Role> response = new Response<>();
        response.setData(roleService.findById(id));
        response.setMessage("Role fetched successfully");
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Role> update(@PathVariable int id, @RequestBody @Validated Role updateRequest) {
        roleService.update(id, updateRequest);
        return new Response<>("Role updated successfully");
    }
}
