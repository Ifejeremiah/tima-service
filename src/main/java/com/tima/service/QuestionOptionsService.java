package com.tima.service;

import com.tima.dao.QuestionOptionDao;
import com.tima.model.Question;
import com.tima.model.QuestionOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Service
public class QuestionOptionsService {
    QuestionOptionDao questionOptionDao;


    public QuestionOptionsService(QuestionOptionDao questionOptionDao) {
        this.questionOptionDao = questionOptionDao;
    }

    public void create(Question question) {
        try {
            for (String option : question.getOptions()) {
                QuestionOptions questionOptions = new QuestionOptions();
                questionOptions.setQuestionId(question.getId());
                questionOptions.setOptions(option);
                questionOptionDao.create(questionOptions);
            }
        } catch (Exception error) {
            log.error("Error creating question option map", error);
            throw error;
        }
    }

    public Question fetchOptions(Question question) {
        try {
            List<QuestionOptions> options = questionOptionDao.findAll(question.getId());
            LinkedHashSet<String> optionSet = new LinkedHashSet<>();
            for (QuestionOptions option : options) {
                optionSet.add(option.getOptions());
            }
            question.setOptions(optionSet);
            return question;
        } catch (Exception error) {
            log.error("Error fetching all question option map", error);
            throw error;
        }
    }

    public void update(Question question) {
        try {
            this.delete(question.getId());
            this.create(question);
        } catch (Exception error) {
            log.error("Error updating question option map", error);
            throw error;
        }
    }

    public void delete(Integer questionId) {
        try {
            questionOptionDao.delete(questionId);
        } catch (Exception error) {
            log.error("Error deleting question option map", error);
            throw error;
        }
    }
}
