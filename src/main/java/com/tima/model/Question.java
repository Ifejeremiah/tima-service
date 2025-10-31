package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.enums.*;
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
    private QuestionStatus status;
    private ExamType examType;
    private String examYear;
    private JobStatus jobStatus;
    private String statusMessage;
}
