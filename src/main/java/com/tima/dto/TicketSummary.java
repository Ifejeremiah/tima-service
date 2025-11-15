package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketSummary {
    private String totalTickets;
    private String openTickets;
    private String inProgressTickets;
    private String resolvedTickets;
}
