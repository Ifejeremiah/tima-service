package com.tima.service;

import com.tima.dao.QuestionDao;
import com.tima.dto.QuestionCreateRequest;
import com.tima.dto.QuestionSummary;
import com.tima.dto.UploadQuestionResponse;
import com.tima.enums.QuestionDifficultyLevel;
import com.tima.enums.QuestionMode;
import com.tima.enums.QuestionStatus;
import com.tima.exception.BadRequestException;
import com.tima.exception.NotFoundException;
import com.tima.model.Page;
import com.tima.model.Question;
import com.tima.util.AuthUtil;
import com.tima.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class QuestionService extends BaseService {

    QuestionDao questionDao;
    QuestionOptionsService questionOptionsService;
    FileService fileService;
    JobService jobService;

    public QuestionService(QuestionDao questionDao, QuestionOptionsService questionOptionsService, FileService fileService, JobService jobService) {
        this.questionDao = questionDao;
        this.questionOptionsService = questionOptionsService;
        this.fileService = fileService;
        this.jobService = jobService;
    }

    public void create(QuestionCreateRequest request) {
        try {
            validateQuestionOptions(request);
            Question question = transformQuestion(request);
            question.setCreatedBy(AuthUtil.getCurrentUserEmail());
            int questionId = (int) questionDao.create(question);
            question.setId(questionId);
            questionOptionsService.create(question);
        } catch (Exception error) {
            log.error("Error creating question", error);
            throw error;
        }
    }

    private void validateQuestionOptions(QuestionCreateRequest request) {
        if (request.getOptions() == null || request.getOptions().isEmpty()) {
            throw new BadRequestException("Options were not provided");
        }
    }

    private Question transformQuestion(QuestionCreateRequest request) {
        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setDifficultyLevel(QuestionDifficultyLevel.valueOf(request.getDifficultyLevel()));
        question.setMode(QuestionMode.valueOf(request.getMode()));
        question.setStatus(QuestionStatus.valueOf(request.getStatus()));
        return question;
    }

    public UploadQuestionResponse upload(MultipartFile file) {
        try {
            checkIfFileIsEmpty(file);
            Long jobId = jobService.create(file.getOriginalFilename());
            fileService.saveFile(file, jobId);
            return new UploadQuestionResponse(jobId);
        } catch (Exception error) {
            log.error("Error uploading file", error);
            throw error;
        }
    }

    private void checkIfFileIsEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file should not be empty");
        }
    }

    public Page<Question> findAll(int page, int size, String searchQuery, String subject, String mode, String difficultyLevel, String examType, String startDate, String endDate) {
        try {
            DateUtil.validateStartAndEndDates(startDate, endDate);
            return questionDao.findAll(page, size, searchQuery, subject, mode, difficultyLevel, examType, startDate, endDate);
        } catch (Exception error) {
            log.error("Error fetching all questions", error);
            throw error;
        }
    }

    public List<Question> findAllForQuiz(int size, String subject, String topic, String difficultyLevel) {
        try {
            return questionDao.findAllForQuiz(size, subject, topic, difficultyLevel);
        } catch (Exception error) {
            log.error("Error fetching all questions for quiz", error);
            throw error;
        }
    }

    public List<Question> findAllSubjects() {
        try {
            return questionDao.findAllSubjects();
        } catch (Exception error) {
            log.error("Error fetching all subjects", error);
            throw error;
        }
    }

    public List<Question> findAllTopicsBySubject(String subject) {
        try {
            return questionDao.findAllTopicsBySubject(subject);
        } catch (Exception error) {
            log.error("Error fetching all topics by subject", error);
            throw error;
        }
    }


    public Question findById(int id) {
        try {
            Question question = questionDao.find(id);
            if (question == null) throw new NotFoundException("Could not find question with question id " + id);
            return questionOptionsService.fetchOptions(question);
        } catch (Exception error) {
            log.error("Error fetching question", error);
            throw error;
        }
    }

    public void update(int id, QuestionCreateRequest update) {
        try {
            Question existing = this.findById(id);
            validateQuestionOptions(update);
            Question question = transformQuestion(update);
            question.setId(existing.getId());
            question.setLastUpdatedBy(AuthUtil.getCurrentUserEmail());
            questionDao.update(question);
            questionOptionsService.update(question);
        } catch (Exception error) {
            log.error("Error updating question", error);
            throw error;
        }
    }

    public void delete(int id) {
        try {
            Question existing = this.findById(id);
            questionOptionsService.delete(existing.getId());
            questionDao.delete(existing.getId());
        } catch (Exception error) {
            log.error("Error deleting question", error);
            throw error;
        }
    }

    public QuestionSummary findQuestionSummary() {
        try {
            return questionDao.findQuestionSummary();
        } catch (Exception error) {
            log.error("Error fetching question summary", error);
            throw error;
        }
    }
}
