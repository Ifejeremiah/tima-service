package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SubmitQuizRequest {
    @Min(10)
    @Max(500)
    @NotNull(message = "Score is required")
    private Integer score;
}
