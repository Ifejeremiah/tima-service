package com.tima.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class TicketCreateRequest {
    @NotBlank(message = "Title is required")
    @Length(max = 100)
    private String title;
    @NotBlank(message = "Priority is required")
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|URGENT)$", message = "Only LOW, MEDIUM, HIGH or URGENT values are allowed")
    private String priority;
    @NotBlank(message = "Category is required")
    @Length(max = 12)
    private String category;
}
