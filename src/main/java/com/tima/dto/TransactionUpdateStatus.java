package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class TransactionUpdateStatus {
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(COMPLETE|FAILED|PENDING|REVERSED)$", message = "Only COMPLETE, FAILED, PENDING or REVERSED values are allowed")
    private String status;
}
