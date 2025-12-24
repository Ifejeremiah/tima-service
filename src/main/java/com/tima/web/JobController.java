package com.tima.web;

import com.tima.dto.Response;
import com.tima.model.Job;
import com.tima.model.Page;
import com.tima.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/jobs")
public class JobController {
    JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<Job> upload(@Validated Job job, @RequestParam MultipartFile file) {
        Response<Job> response = new Response<>();
        response.setData(jobService.upload(job, file));
        response.setMessage("Job uploaded successfully");
        return response;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Page<Job>> findAll(
            @RequestParam(name = "page_num", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int size,
            @RequestParam(name = "status", required = false) String status) {
        Response<Page<Job>> response = new Response<>();
        response.setData(jobService.findAll(page, size, status));
        response.setMessage("Jobs fetched successfully");
        return response;
    }

    @GetMapping(path = "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Job> find(@PathVariable String requestId) {
        Response<Job> response = new Response<>();
        response.setData(jobService.findByRequestId(requestId));
        response.setMessage("Job fetched successfully");
        return response;
    }
}
