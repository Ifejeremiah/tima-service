package com.tima.web;

import com.tima.dto.QuestionCreateRequest;
import com.tima.dto.Response;
import com.tima.dto.UploadQuestionResponse;
import com.tima.model.Page;
import com.tima.model.Question;
import com.tima.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/questions")
public class QuestionController {
    QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Question> create(@Validated @RequestBody QuestionCreateRequest request) {
        questionService.create(request);
        return new Response<>("Question created successfully");
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UploadQuestionResponse> upload(@RequestParam MultipartFile file) {
        Response<UploadQuestionResponse> response = new Response<>();
        response.setData(questionService.upload(file));
        response.setMessage("Bulk questions uploaded successfully");
        return response;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<Question>> findAll(
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size,
            @RequestParam(name = "search_query", required = false) String searchQuery) {
        Response<Page<Question>> response = new Response<>();
        response.setData(questionService.findAll(page, size, searchQuery));
        response.setMessage("Questions fetched successfully");
        return response;
    }

    @GetMapping(path = "/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<Question>> findAllForQuiz(
            @RequestParam(name = "page_size", defaultValue = "10") int size,
            @RequestParam(name = "subject") String subject,
            @RequestParam(name = "topic") String topic,
            @RequestParam(name = "difficultyLevel") String difficultyLevel) {
        Response<List<Question>> response = new Response<>();
        response.setData(questionService.findAllForQuiz(size, subject, topic, difficultyLevel));
        response.setMessage("Questions for quiz fetched successfully");
        return response;
    }

    @GetMapping(path = "/subjects", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<Question>> findAllSubjects() {
        Response<List<Question>> response = new Response<>();
        response.setData(questionService.findAllSubjects());
        response.setMessage("Subjects fetched successfully");
        return response;
    }

    @GetMapping(path = "/topics", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<Question>> findAllTopicsBySubject(
            @RequestParam(name = "subject") String subject) {
        Response<List<Question>> response = new Response<>();
        response.setData(questionService.findAllTopicsBySubject(subject));
        response.setMessage("Topics by subject fetched successfully");
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Question> find(@PathVariable int id) {
        Response<Question> response = new Response<>();
        response.setData(questionService.findById(id));
        response.setMessage("Question fetched successfully");
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Question> update(@PathVariable int id, @RequestBody @Validated Question updateRequest) {
        questionService.update(id, updateRequest);
        return new Response<>("Question updated successfully");
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Question> delete(@PathVariable int id) {
        questionService.delete(id);
        return new Response<>("Question deleted successfully");
    }
}
