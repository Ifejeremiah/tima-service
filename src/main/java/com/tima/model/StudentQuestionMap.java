package com.tima.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentQuestionMap extends BaseObject {
    private String studentId;
    private String questionId;
    private Integer answer;
}
