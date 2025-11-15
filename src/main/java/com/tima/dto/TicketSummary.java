package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketSummary {
    private Integer totalTickets;
    private Integer openTickets;
    private Integer inProgressTickets;
    private Integer resolvedTickets;
}
