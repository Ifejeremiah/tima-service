package com.tima.web;

import com.tima.enums.QuestionBankStatus;
import com.tima.model.QuestionBank;
import com.tima.model.Response;
import com.tima.service.QuestionBankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/questions")
public class QuestionBankController {
    QuestionBankService questionBankService;

    public QuestionBankController(QuestionBankService questionBankService) {
        this.questionBankService = questionBankService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<QuestionBank> create(@Validated @RequestBody QuestionBank questionBank) {
        questionBankService.create(questionBank);
        return new Response<>("Question created successfully");
    }

    @GetMapping(path = "test", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<QuestionBank>> fetchQuestionList(
            @RequestParam(name = "subject") String subject,
            @RequestParam(name = "topic") String topic,
            @RequestParam(name = "difficulty") QuestionBankStatus difficulty,
            @RequestParam(name = "limit") int limit
    ) {
        Response<List<QuestionBank>> response = new Response<>();
        response.setData(questionBankService.fetchQuestionList(subject, topic, difficulty, limit));
        response.setMessage("Question for test fetched successfully");
        return response;
    }

//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public Response<Page<QuestionBank>> findAll(
//            @RequestParam(name = "page_num", defaultValue = "0") int page,
//            @RequestParam(name = "page_size", defaultValue = "10") int size) {
//        Response<Page<QuestionBank>> response = new Response<>();
//        response.setData(questionBankService.findAll(page, size));
//        response.setMessage("Question fetched successfully");
//        return response;
//    }
}
