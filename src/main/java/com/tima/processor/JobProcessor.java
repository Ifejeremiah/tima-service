package com.tima.processor;

import com.tima.dao.JobDao;
import com.tima.dao.QuestionDao;
import com.tima.enums.JobStatus;
import com.tima.io.FileReader;
import com.tima.model.Job;
import com.tima.model.Question;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class JobProcessor implements Runnable {
    Job job;
    JobDao jobDao;
    String location;
    QuestionDao questionDao;

    public JobProcessor(Job job, JobDao jobDao, QuestionDao questionDao, String location) {
        this.job = job;
        this.jobDao = jobDao;
        this.questionDao = questionDao;
        this.location = location;
    }


    public void run() {
        checkIfFileExists();
        StringBuilder statusMessage = new StringBuilder();

        try (InputStream inputStream = Files.newInputStream(getFilePath())) {
            FileReader reader = new FileReader(inputStream);
            for (Question question = new Question(); reader.hasNext(question); ) {
                if (JobStatus.FAILED.equals(question.getStatus())) {
                    statusMessage.append(question.getStatusMessage());
                } else {
                    question.setCreatedBy(job.getCreatedBy());
                    questionDao.create(question);
                    jobDao.updateJobStatus(job.getId(), JobStatus.SUCCESSFUL, question.getStatusMessage());
                }
            }

            if (statusMessage.length() > 0) {
                jobDao.updateJobStatus(job.getId(), JobStatus.FAILED, statusMessage.toString());
            }
        } catch (Exception error) {
            String msg = "Error processing file";
            jobDao.updateJobStatus(job.getId(), JobStatus.FAILED, msg + ":\n\t" + error);
            log.error(msg, error);
        }
    }

    public void checkIfFileExists() {
        String filePath = getFilePath().toString();
        if (!new File(getFilePath().toString()).exists()) {
            String errorMessage = "Could not find file in path " + filePath;
            jobDao.updateJobStatus(job.getId(), JobStatus.FAILED, errorMessage);
            log.error(errorMessage);
        }
    }

    private Path getFilePath() {
        return Paths.get(location, job.getId().toString(), "file_upload.txt");
    }
}
