package com.tima.web;

import com.tima.dto.ChangePasswordRequest;
import com.tima.dto.Response;
import com.tima.model.Page;
import com.tima.model.User;
import com.tima.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
    UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<User>> findAll(
            @RequestParam(name = "page_num", defaultValue = "1") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size,
            @RequestParam(name = "search_query", required = false) String searchQuery) {
        Response<Page<User>> response = new Response<>();
        response.setData(userService.findAll(page, size, searchQuery));
        response.setMessage("Users fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<User> find(@PathVariable int id) {
        Response<User> response = new Response<>();
        response.setData(userService.findById(id));
        response.setMessage("User fetched successfully");
        return response;
    }

    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<User> findByCurrentUser() {
        Response<User> response = new Response<>();
        response.setData(userService.findByCurrentUser());
        response.setMessage("User fetched by current user successfully");
        return response;
    }

    @PutMapping(path = "/me/passwords", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<User> changePassword(@RequestBody @Validated ChangePasswordRequest request) {
        userService.changePassword(request);
        return new Response<>("Password updated successfully");
    }

    @DeleteMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<User> deleteByCurrentUser() {
        userService.deleteByCurrentUser();
        return new Response<>("User deleted by current user successfully");
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<User> delete(@PathVariable int id) {
        userService.delete(id);
        return new Response<>("User deleted successfully");
    }
}
