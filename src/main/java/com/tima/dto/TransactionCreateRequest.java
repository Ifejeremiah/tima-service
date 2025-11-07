package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionCreateRequest {
    @NotNull(message = "Customer ID is required")
    private Integer customerId;
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(COMPLETE|FAILED|PENDING|REVERSED)$", message = "Only COMPLETE, FAILED, PENDING or REVERSED values are allowed")
    private String status;
    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 2)
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;
    @NotBlank(message = "Payment plan is required")
    private String paymentPlan;
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    @NotNull(message = "Next payment is required")
    @Future(message = "Next payment cannot be in the past")
    private LocalDateTime nextPaymentOn;

}
