package com.tima.service;

import com.tima.dao.QuestionDao;
import com.tima.exception.NotFoundException;
import com.tima.model.Page;
import com.tima.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuestionService extends BaseService {

    QuestionDao questionDao;
    UserService userService;

    public QuestionService(QuestionDao questionDao, UserService userService) {
        this.questionDao = questionDao;
        this.userService = userService;
    }

    public void create(Question question) {
        try {
            question.setCreatedBy(getCurrentUserEmail());
            questionDao.create(question);
        } catch (Exception error) {
            log.error("Error creating question", error);
            throw error;
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
