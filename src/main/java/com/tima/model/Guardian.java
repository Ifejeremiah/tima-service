package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Document(collection = "guardians")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Guardian {
    @NotBlank(message = "Name is required")
    @Length(min = 3, max = 40)
    private String name;
    private Boolean agreeToTerms;
    private Boolean canNotify;
}
