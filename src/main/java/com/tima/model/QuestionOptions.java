package com.tima.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionOptions extends BaseObject {
    private Integer questionId;
    private String options;
}
