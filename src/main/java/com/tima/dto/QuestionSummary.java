package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionSummary {
    private Integer totalQuestions;
    private Integer totalPracticeQuestions;
    private Integer totalExamQuestions;
    private Integer totalDraftQuestions;
}
