package com.tima.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.enums.JobStatus;
import com.tima.enums.QuestionDifficultyLevel;
import com.tima.enums.QuestionMode;
import com.tima.enums.QuestionStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.LinkedHashSet;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Question extends BaseObject {
    private String question;
    private LinkedHashSet<String> optionList;
    @JsonIgnore
    private String options;
    private String answer;
    private String subject;
    private String topic;
    private QuestionDifficultyLevel difficultyLevel;
    private QuestionMode mode;
    private QuestionStatus status;
    private String examType;
    private String examYear;
    private JobStatus jobStatus;
    private String statusMessage;
    private Integer jobId;

    public LinkedHashSet<String> getOptionList() {
        if (options == null) return null;
        String[] optionList = options.trim().split("\\|");
        return new LinkedHashSet<>(Arrays.asList(optionList));
    }

    public void setOptions(LinkedHashSet<String> fields) {
        this.options = String.join("|", fields);
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
