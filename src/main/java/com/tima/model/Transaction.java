package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Transaction extends BaseObject {
    private String transactionRef;
    private Integer customerId;
    private String status;
    private BigDecimal amount;
    private String paymentPlan;
    private String paymentMethod;
    private LocalDateTime nextPaymentOn;
}
