package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class TicketUpdateRequest {
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(OPEN|IN_PROGRESS|RESOLVED)$", message = "Only OPEN, IN_PROGRESS or RESOLVED values are allowed")
    private String status;
    @NotNull(message = "Assigned To is required")
    private Integer assignedTo;
}
