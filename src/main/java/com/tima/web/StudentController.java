package com.tima.web;

import com.tima.model.Response;
import com.tima.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/students")
public class StudentController {
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Student> register(@Validated @RequestBody Student student) {
        return new Response<>("Student created successfully");
    }
}
