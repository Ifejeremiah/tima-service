package com.tima.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "student_question_map")
public class StudentQuestionMap extends BaseObject {
    private String studentId;
    private String questionId;
    private Integer answer;
}
