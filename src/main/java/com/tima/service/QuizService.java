package com.tima.service;

import com.tima.dao.QuizDao;
import com.tima.dto.QuizResponse;
import com.tima.exception.BadRequestException;
import com.tima.exception.NotFoundException;
import com.tima.io.QuizMarker;
import com.tima.model.Page;
import com.tima.model.Quiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuizService {
    QuizDao quizDao;
    StudentService studentService;
    QuestionService questionService;
    QuestionQuizService questionQuizService;
    TaskExecutor executor;

    public QuizService(QuizDao quizDao, StudentService studentService, QuestionService questionService, QuestionQuizService questionQuizService, @Qualifier("executor") TaskExecutor executor) {
        this.quizDao = quizDao;
        this.studentService = studentService;
        this.questionService = questionService;
        this.questionQuizService = questionQuizService;
        this.executor = executor;
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

    public void update(int id, Quiz update) {
        try {
            Quiz existing = this.findById(id);
            update.setId(existing.getId());
            quizDao.update(update);
        } catch (Exception error) {
            log.error("Error updating quiz", error);
            throw error;
        }
    }

    public void submit(int id, List<QuizResponse> responses) {
        try {
            if (responses.isEmpty()) throw new BadRequestException("Response list is empty");
            Quiz quiz = this.findById(id);
            if (quiz.getScore() != null) throw new BadRequestException("Quiz has been submitted already");
            QuizMarker marker = new QuizMarker(responses, this, questionService, questionQuizService, id);
            executor.execute(marker);
        } catch (Exception error) {
            log.error("Error submitting quiz", error);
            throw error;
        }
    }

    private int getCurrentStudentId() {
        return studentService.findByUserId().getId();
    }
}
