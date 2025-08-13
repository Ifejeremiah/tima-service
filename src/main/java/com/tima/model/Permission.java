package com.tima.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Permission extends BaseObject {
    @NotBlank(message = "Name is required")
    @Length(min = 5, max = 40)
    private String name;
    @NotBlank(message = "Code is required")
    @Length(min = 5, max = 40)
    private String code;
}
