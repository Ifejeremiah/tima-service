package com.tima.service;

import com.tima.dao.JobDao;
import com.tima.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JobService {
    JobDao jobDao;

    public JobService(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    public long create(Job job) {
        try {
            return jobDao.create(job);
        } catch (Exception error) {
            log.error("Error creating job", error);
            throw error;
        }
    }
}
