package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.enums.TicketPriority;
import com.tima.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Ticket extends BaseObject {
    private String title;
    private TicketStatus status;
    private TicketPriority priority;
    private String category;
    private Integer assignedTo;
}
