package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TicketChat extends BaseObject {
    private Integer ticketId;
    private Integer userId;
    private String name;
    private String email;
    @NotBlank(message = "Text is required")
    private String text;
}
