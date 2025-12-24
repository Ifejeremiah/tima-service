package com.tima.processor;

import com.tima.dao.JobDao;
import com.tima.enums.JobStatus;
import com.tima.io.ExcelParser;
import com.tima.model.ExcelRow;
import com.tima.model.Job;
import com.tima.model.Question;
import com.tima.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class FileProcessor implements Runnable {
    Job job;
    JobDao jobDao;
    QuestionService questionService;
    int recordIndex;
    int recordSize;
    StringBuilder script;

    public FileProcessor(Job job, JobDao jobDao, QuestionService questionService, int recordSize) {
        this.job = job;
        this.jobDao = jobDao;
        this.questionService = questionService;
        this.recordIndex = 0;
        this.recordSize = recordSize;
        this.script = new StringBuilder();
    }

    public void run() {
        try {
            ExcelParser reader = new ExcelParser();
            List<ExcelRow> rows = reader.read(streamFile());
            for (ExcelRow row : rows) {
                Question question = new Question();

                question.setJobStatus(JobStatus.SUCCESSFUL);
                if (StringUtils.isEmpty(row.getCol1()) || StringUtils.isEmpty(row.getCol2()) || StringUtils.isEmpty(row.getCol3())) {
                    question.setJobStatus(JobStatus.FAILED);
                    question.setStatusMessage("Failed to parse record at line " + row.getRowNumber() + ": One or more data not provided. Please correct.");
                }
                question.setQuestion(row.getCol1());
                question.setOptions(row.getCol2());
                question.setAnswer(row.getCol3());

                script.append(questionService.formatObjectAsQuery(question, job)).append(",");
                recordIndex++;

                if (recordIndex == recordSize) {
                    loadDataToDB();
                    recordIndex = 0;
                    script.setLength(0);
                }
            }
            if (recordIndex > 0) {
                loadDataToDB();
            }
            jobDao.updateJobStatusAndRecordCount(job.getId(), JobStatus.SUCCESSFUL, rows.size());
            cleanup();
        } catch (Exception error) {
            String msg = "Error processing file";
            jobDao.updateJobStatus(job.getId(), JobStatus.FAILED, msg + ":\n\t" + error);
            log.error(msg, error);
        }
    }

    private InputStream streamFile() throws IOException {
        return Files.newInputStream(Paths.get(job.getFilePath()));
    }

    private void loadDataToDB() {
        String columns = "(job_id,question,options,answer,subject,topic,difficulty_level,mode,status,exam_type,exam_year,created_by,created_on,job_status,status_message)";
        String headerScript = String.format("INSERT INTO [tbl_questions] %s SELECT * FROM (VALUES ", columns);
        String footerScript = String.format(") x %s", columns);
        executeQuery(headerScript, script, footerScript);
    }

    private void executeQuery(String headerScript, StringBuilder bodyScript, String footerScript) {
        StringBuilder script = new StringBuilder(headerScript);
        script.append(bodyScript);
        script.setCharAt(script.length() - 1, ' ');
        script.append(footerScript);
        jobDao.execute(script.toString());
    }

    private void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(job.getFilePath()));
    }
}
