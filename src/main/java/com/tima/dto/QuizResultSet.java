package com.tima.dto;

import com.tima.model.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizResultSet {
    private Integer quizId;
    private Integer count;
    private List<Question> questionList;
}
