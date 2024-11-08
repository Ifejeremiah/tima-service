package com.tima.web;

import com.tima.model.Response;
import com.tima.model.Student;
import com.tima.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Response<List<Student>> findAll(
            @RequestParam(name = "page_num", defaultValue = "1") int page,
            @RequestParam(name = "page_size", defaultValue = "0") int size) {
        Response<List<Student>> response = new Response<>();
        response.setData(studentService.findAll(page, size));
        response.setResponseMessage("Students fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Student> find(@PathVariable String id) {
        Response<Student> response = new Response<>();
        response.setData(studentService.findById(id));
        response.setResponseMessage("Student fetched successfully");
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Student> update(@RequestBody @Validated Student updateRequest, @PathVariable String id) {
        studentService.update(id, updateRequest);
        return new Response<>("Student updated successfully");
    }

//    @PreAuthorize("hasRole('DeletePerso')")
//    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Response<Perso> delete(@PathVariable int id) {
//        persoService.delete(id);
//        return new Response<>("Perso deleted successfully");
//    }
}
