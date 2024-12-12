package com.tima.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionQuiz extends BaseObject {
    private Integer questionId;
    private Integer quizId;
    private String answer;
}
