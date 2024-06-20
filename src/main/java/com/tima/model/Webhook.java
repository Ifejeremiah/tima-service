package com.tima.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Webhook {
    @NotBlank(message = "Ref is required")
    @Length(min = 10, max = 30)
    private String ref;
}
