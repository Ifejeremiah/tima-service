package com.tima.service;

import com.tima.dao.QuestionDao;
import com.tima.dto.QuestionCreateRequest;
import com.tima.dto.UploadQuestionResponse;
import com.tima.enums.ExamType;
import com.tima.enums.QuestionDifficultyLevel;
import com.tima.enums.QuestionMode;
import com.tima.exception.BadRequestException;
import com.tima.exception.NotFoundException;
import com.tima.model.Page;
import com.tima.model.Question;
import com.tima.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class QuestionService extends BaseService {

    QuestionDao questionDao;
    UserService userService;
    FileService fileService;
    JobService jobService;

    public QuestionService(QuestionDao questionDao, UserService userService, FileService fileService, JobService jobService) {
        this.questionDao = questionDao;
        this.userService = userService;
        this.fileService = fileService;
        this.jobService = jobService;
    }

    public void create(QuestionCreateRequest request) {
        try {
            validateQuestionOptions(request);
            questionDao.create(buildQuestion(request));
        } catch (Exception error) {
            log.error("Error creating question", error);
            throw error;
        }
    }

    private void validateQuestionOptions(QuestionCreateRequest request){
        if (request.getOptions() == null || request.getOptions().isEmpty()) {
            throw new BadRequestException("Options are required to create a question");
        }
    }

    private Question buildQuestion(QuestionCreateRequest request){
        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setDifficultyLevel(QuestionDifficultyLevel.valueOf(request.getDifficultyLevel()));
        question.setMode(QuestionMode.valueOf(request.getMode()));
        question.setExamType(ExamType.valueOf(request.getExamType()));
        question.setCreatedBy(AuthUtil.getCurrentUserEmail());
        return question;
    }

    public UploadQuestionResponse upload(MultipartFile file){
        checkIfFileIsEmpty(file);
        Long jobId = jobService.create();
        fileService.saveFile(file, jobId);
        return new UploadQuestionResponse(jobId);
    }

    private void checkIfFileIsEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file should not be empty");
        }
    }

    public Page<Question> findAll(int page, int size, String searchQuery) {
        try {
            return questionDao.findAll(page, size, searchQuery);
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
            return question;
        } catch (Exception error) {
            log.error("Error fetching question", error);
            throw error;
        }
    }

    public void update(int id, Question update) {
        try {
            Question existing = this.findById(id);
            update.setId(existing.getId());
            update.setLastUpdatedBy(getCurrentUserEmail());
            questionDao.update(update);
        } catch (Exception error) {
            log.error("Error updating question", error);
            throw error;
        }
    }

    public void delete(int id) {
        try {
            Question existing = this.findById(id);
            questionDao.delete(existing.getId());
        } catch (Exception error) {
            log.error("Error deleting question", error);
            throw error;
        }
    }

    private String getCurrentUserEmail() {
        return userService.findByCurrentUser().getEmail();
    }
}
