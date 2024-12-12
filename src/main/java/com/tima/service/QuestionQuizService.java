package com.tima.service;

import com.tima.dao.QuestionQuizDao;
import com.tima.model.QuestionQuiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuestionQuizService extends BaseService {
    QuestionQuizDao questionQuizDao;

    public QuestionQuizService(QuestionQuizDao questionQuizDao) {
        this.questionQuizDao = questionQuizDao;
    }

    public void create(QuestionQuiz questionQuiz) {
        try {
            questionQuizDao.create(questionQuiz);
        } catch (Exception error) {
            log.error("Error creating question quiz map", error);
            throw error;
        }
    }
}
