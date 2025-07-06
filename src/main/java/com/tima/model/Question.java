package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.enums.ExamType;
import com.tima.enums.QuestionDifficultyLevel;
import com.tima.enums.QuestionMode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Question extends BaseObject {
    private String question;
    private Set<String> options;
    private String answer;
    private String subject;
    private String topic;
    private QuestionDifficultyLevel difficultyLevel;
    private QuestionMode mode;
    private ExamType examType;
}
