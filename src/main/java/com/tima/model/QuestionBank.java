package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuestionBank extends BaseObject {
    @NotBlank(message = "Subject is required")
    @Length(min = 3, max = 100)
    private String subject;
    @NotBlank(message = "Topic is required")
    @Length(min = 3, max = 240)
    private String topic;
    @NotBlank(message = "Difficulty is required")
    @Length(min = 3, max = 10)
    private String difficulty;
    @NotBlank(message = "Question is required")
    private String question;
    private Set<String> options;
    @NotNull(message = "Answer is required")
    private Integer answer;
}
