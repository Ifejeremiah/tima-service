package com.tima.service;

import com.tima.dao.QuestionDao;
import com.tima.dto.QuestionCreateRequest;
import com.tima.dto.QuestionSummary;
import com.tima.enums.QuestionDifficultyLevel;
import com.tima.enums.QuestionMode;
import com.tima.enums.QuestionStatus;
import com.tima.exception.BadRequestException;
import com.tima.exception.NotFoundException;
import com.tima.model.Job;
import com.tima.model.Page;
import com.tima.model.Question;
import com.tima.util.AuthUtil;
import com.tima.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuestionService extends BaseService {

    QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public void create(QuestionCreateRequest request) {
        try {
            validateQuestionOptions(request);
            Question question = transformQuestion(request);
            int questionId = (int) questionDao.create(question);
            question.setId(questionId);
            questionDao.create(question);
        } catch (Exception error) {
            log.error("Error creating question", error);
            throw error;
        }
    }

    private void validateQuestionOptions(QuestionCreateRequest request) {
        if (request.getOptionList() == null || request.getOptionList().isEmpty()) {
            throw new BadRequestException("Option list was not provided");
        }
    }

    private Question transformQuestion(QuestionCreateRequest request) {
        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setOptions(request.getOptionList());
        question.setDifficultyLevel(QuestionDifficultyLevel.valueOf(request.getDifficultyLevel()));
        question.setMode(QuestionMode.valueOf(request.getMode()));
        question.setStatus(QuestionStatus.valueOf(request.getStatus()));
        question.setCreatedBy(AuthUtil.getCurrentUserEmail());
        return question;
    }

    public String formatObjectAsQuery(Question question, Job job) {
        return String.format("(%d,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',GETDATE(),'%s','%s')",
                job.getId(),
                question.getQuestion(),
                question.getOptions(),
                question.getAnswer(),
                job.getSubject(),
                job.getTopic(),
                job.getDifficultyLevel(),
                job.getMode(),
                job.getStatus(),
                job.getExamType(),
                job.getExamYear(),
                job.getCreatedBy(),
                question.getJobStatus(),
                question.getStatusMessage() == null ? "" : question.getStatusMessage());
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
            return question;
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

    public QuestionSummary findQuestionSummary() {
        try {
            return questionDao.findQuestionSummary();
        } catch (Exception error) {
            log.error("Error fetching question summary", error);
            throw error;
        }
    }
}
