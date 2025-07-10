package com.tima.service;

import com.tima.dao.JobDao;
import com.tima.dao.QuestionDao;
import com.tima.model.Job;
import com.tima.processor.JobProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SchedulerService {
    @Value("${JOB.FETCH.COUNT}")
    private Integer fetchCount;
    @Value("${JOB.AMNESTY.TIME}")
    public Integer amnestyTime;
    JobDao jobDao;
    TaskExecutor executor;
    FileService fileService;
    QuestionDao questionDao;
    @Value("${FILE.DIRECTORY}")
    private String location;

    public SchedulerService(JobDao jobDao, @Qualifier("executor") TaskExecutor executor, FileService fileService, QuestionDao questionDao) {
        this.jobDao = jobDao;
        this.executor = executor;
        this.fileService = fileService;
        this.questionDao = questionDao;
    }

    @Scheduled(fixedDelayString = "${SCHEDULER.DELAY}")
    public void processJobs() {
        List<Job> jobs = jobDao.findJobsForProcessing(fetchCount, amnestyTime);
        log.info("{} Jobs picked for processing", jobs.size());
        for (Job job : jobs) {
            JobProcessor processor = new JobProcessor(job, jobDao, questionDao, location);
            executor.execute(processor);
        }
    }
}
