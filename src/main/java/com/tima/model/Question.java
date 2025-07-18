package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.enums.ExamType;
import com.tima.enums.JobStatus;
import com.tima.enums.QuestionDifficultyLevel;
import com.tima.enums.QuestionMode;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Question extends BaseObject {
    private String question;
    private LinkedHashSet<String> options;
    private String answer;
    private String subject;
    private String topic;
    private QuestionDifficultyLevel difficultyLevel;
    private QuestionMode mode;
    private ExamType examType;
    private JobStatus status;
    private String statusMessage;
}
