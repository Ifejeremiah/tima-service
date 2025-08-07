package com.tima.dto;

import com.tima.model.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizResponse {
    private Integer quizId;
    private Integer studentId;
    private Integer count;
    private List<Question> questions;

    public QuizResponse(Integer quizId, Integer studentId, Integer count, List<Question> questions) {
        this.quizId = quizId;
        this.studentId = studentId;
        this.count = count;
        this.questions = questions;
    }
}
