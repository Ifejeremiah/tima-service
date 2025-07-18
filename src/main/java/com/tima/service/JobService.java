package com.tima.service;

import com.tima.dao.JobDao;
import com.tima.enums.JobStatus;
import com.tima.model.Job;
import com.tima.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JobService {
    JobDao jobDao;

    public JobService(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    public long create(String originalFileName) {
        try {
            Job job = new Job();
            job.setStatus(JobStatus.NEW);
            job.setOriginalFileName(originalFileName);
            job.setCreatedBy(AuthUtil.getCurrentUserEmail());
            return jobDao.create(job);
        } catch (Exception error) {
            log.error("Error creating job", error);
            throw error;
        }
    }
}
