package com.tima.web;

import com.tima.dto.Response;
import com.tima.model.Page;
import com.tima.model.Student;
import com.tima.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/students")
public class StudentController {
    StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Student> create(@Validated @RequestBody Student student) {
        studentService.create(student);
        return new Response<>("Student created successfully");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<Student>> findAll(
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size,
            @RequestParam(name = "search_query", required = false) String searchQuery) {
        Response<Page<Student>> response = new Response<>();
        response.setData(studentService.findAll(page, size, searchQuery));
        response.setMessage("Students fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Student> find(@PathVariable int id) {
        Response<Student> response = new Response<>();
        response.setData(studentService.findById(id));
        response.setMessage("Student fetched successfully");
        return response;
    }


    @PutMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Student> update(@RequestBody @Validated Student updateRequest) {
        studentService.update(updateRequest);
        return new Response<>("Student updated by current user successfully");
    }
}
