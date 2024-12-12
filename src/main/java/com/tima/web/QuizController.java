package com.tima.web;

import com.tima.dto.QuizResponse;
import com.tima.dto.Response;
import com.tima.model.Page;
import com.tima.model.Quiz;
import com.tima.service.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/quizzes")
public class QuizController {

    QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Quiz> create(@Validated @RequestBody Quiz quiz) {
        Response<Quiz> response = new Response<>();
        response.setData(quizService.create(quiz));
        response.setMessage("Quiz created successfully");
        return response;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<Quiz>> findAll(
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size) {
        Response<Page<Quiz>> response = new Response<>();
        response.setData(quizService.findAll(page, size));
        response.setMessage("Quizzes fetched successfully");
        return response;
    }

    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<Quiz>> findAllByStudentId(
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size) {
        Response<Page<Quiz>> response = new Response<>();
        response.setData(quizService.findAllByStudentId(page, size));
        response.setMessage("Quizzes by Student ID fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Quiz> find(@PathVariable int id) {
        Response<Quiz> response = new Response<>();
        response.setData(quizService.findById(id));
        response.setMessage("Quiz fetched successfully");
        return response;
    }

    @PostMapping(path = "/{id}/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Quiz> submit(@PathVariable int id, @RequestBody List<QuizResponse> responses) {
        quizService.submit(id, responses);
        return new Response<>("Quiz submitted successfully");
    }
}
