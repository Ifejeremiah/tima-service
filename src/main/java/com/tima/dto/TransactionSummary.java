package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionSummary {
    private BigDecimal total;
    private BigDecimal complete;
    private BigDecimal pending;
    private BigDecimal failed;
    private BigDecimal reversed;
}
