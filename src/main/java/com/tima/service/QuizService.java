package com.tima.service;

import com.tima.dao.QuizDao;
import com.tima.dto.QuizResponse;
import com.tima.dto.QuizResultSet;
import com.tima.dto.StartQuizRequest;
import com.tima.dto.SubmitQuizRequest;
import com.tima.enums.QuestionDifficultyLevel;
import com.tima.exception.BadRequestException;
import com.tima.exception.NotFoundException;
import com.tima.model.Page;
import com.tima.model.Question;
import com.tima.model.Quiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;

@Slf4j
@Service
public class QuizService {
    QuizDao quizDao;
    StudentService studentService;
    QuestionOptionsService questionOptionsService;

    public QuizService(QuizDao quizDao, StudentService studentService, QuestionOptionsService questionOptionsService) {
        this.quizDao = quizDao;
        this.studentService = studentService;
        this.questionOptionsService = questionOptionsService;
    }

    public Quiz create(Quiz quiz) {
        try {
            quiz.setStudentId(getCurrentStudentId());
            quiz.setId((int) quizDao.create(quiz));
            return quiz;
        } catch (Exception error) {
            log.error("Error creating quiz", error);
            throw error;
        }
    }

    public Page<Quiz> findAll(int page, int size) {
        try {
            return quizDao.findAll(page, size);
        } catch (Exception error) {
            log.error("Error fetching all quizzes", error);
            throw error;
        }
    }

    public Page<Quiz> findAllByStudentId(int page, int size) {
        try {
            return quizDao.findAllByStudentId(page, size, getCurrentStudentId());
        } catch (Exception error) {
            log.error("Error fetching all quizzes by student id", error);
            throw error;
        }
    }

    public Quiz findById(int id) {
        try {
            Quiz quiz = quizDao.find(id);
            if (quiz == null) throw new NotFoundException("Could not find quiz with quiz id " + id);
            return quiz;
        } catch (Exception error) {
            log.error("Error fetching quiz", error);
            throw error;
        }
    }

    public QuizResponse start(StartQuizRequest quizRequest) {
        try {
            Quiz quiz = new Quiz();
            BeanUtils.copyProperties(quizRequest, quiz);
            quiz.setDifficultyLevel(QuestionDifficultyLevel.valueOf(quizRequest.getDifficultyLevel()));
            quiz.setStudentId(getCurrentStudentId());
            QuizResultSet resultSet = quizDao.start(quiz);
            for (Question question : resultSet.getQuestionList()) {
                LinkedHashSet<String> optionSet = questionOptionsService.fetchOptions(question).getOptions();
                question.setOptions(optionSet);
            }
            return new QuizResponse(resultSet.getQuizId(), getCurrentStudentId(), resultSet.getCount(), resultSet.getQuestionList());
        } catch (Exception error) {
            log.error("Error starting quiz", error);
            throw error;
        }
    }

    public void submit(int id, SubmitQuizRequest submitRequest) {
        try {
            getCurrentStudentId();
            Quiz quiz = this.findById(id);
            if (quiz.getScore() != null) throw new BadRequestException("Quiz has been submitted already");
            quizDao.submit(quiz.getId(), submitRequest.getScore());
        } catch (Exception error) {
            log.error("Error submitting quiz", error);
            throw error;
        }
    }

    private int getCurrentStudentId() {
        return studentService.findByUserId().getId();
    }
}
