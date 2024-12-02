package com.tima.model;

import com.tima.enums.QuestionStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Question extends BaseObject {
    @NotBlank(message = "Subject is required")
    @Length(min = 3, max = 100)
    private String subject;
    @NotBlank(message = "Topic is required")
    @Length(min = 3, max = 100)
    private String topic;
    @NotBlank(message = "Difficulty Level is required")
    @Length(min = 4, max = 7)
    private QuestionStatus difficultyLevel;
    @NotBlank(message = "Title is required")
    @Length(min = 3)
    private String title;
    @NotBlank(message = "Option A is required")
    @Length(min = 3, max = 225)
    private String optionA;
    @NotBlank(message = "Option B is required")
    @Length(min = 3, max = 225)
    private String optionB;
    @NotBlank(message = "Option C is required")
    @Length(min = 3, max = 225)
    private String optionC;
    @NotBlank(message = "Option D is required")
    @Length(min = 3, max = 225)
    private String optionD;
    @NotBlank(message = "Answer is required")
    @Length(min = 3, max = 225)
    private String answer;
}
