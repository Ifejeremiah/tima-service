package com.tima.service;

import com.tima.dao.JobDao;
import com.tima.enums.JobStatus;
import com.tima.exception.NotFoundException;
import com.tima.model.Job;
import com.tima.model.Page;
import com.tima.processor.FileProcessor;
import com.tima.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
public class JobService {
    JobDao jobDao;
    FileService fileService;
    TaskExecutor executor;
    @Value("${RECORD.SIZE}")
    Integer recordSize;
    QuestionService questionService;

    public JobService(JobDao jobDao, FileService fileService, QuestionService questionService, @Qualifier("executor") TaskExecutor executor) {
        this.jobDao = jobDao;
        this.fileService = fileService;
        this.questionService = questionService;
        this.executor = executor;
    }

    public Job upload(Job job, MultipartFile file) {
        try {
            String filePath = fileService.saveFile(file);
            job.setId((int) jobDao.create(buildJob(job, file, filePath)));
            runJob(job);
            return job;
        } catch (Exception error) {
            log.error("Error uploading job", error);
            throw error;
        }
    }

    private Job buildJob(Job job, MultipartFile file, String filePath) {
        job.setRequestId(UUID.randomUUID().toString());
        job.setOriginalFileName(file.getOriginalFilename());
        job.setCreatedBy(AuthUtil.getCurrentUserEmail());
        job.setJobStatus(JobStatus.NEW);
        job.setFilePath(filePath);
        return job;
    }

    private void runJob(Job job) {
        FileProcessor processor = new FileProcessor(job, jobDao, questionService, recordSize);
        executor.execute(processor);
    }

    public Page<Job> findAll(int page, int size, String status) {
        try {
            return jobDao.findAll(page, size, status);
        } catch (Exception error) {
            log.error("Error fetching all jobs", error);
            throw error;
        }
    }

    public Job findByRequestId(String requestId) {
        try {
            Job job = jobDao.findByRequestId(requestId);
            if (job == null) throw new NotFoundException("Could not find job with request id " + requestId);
            return job;
        } catch (Exception error) {
            log.error("Error fetching job", error);
            throw error;
        }
    }
}
