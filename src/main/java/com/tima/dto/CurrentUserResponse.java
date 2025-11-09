package com.tima.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.model.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CurrentUserResponse {
    private User user;
    private AdminUser adminUser;
    private Student student;
    private List<Role> roles;
    private List<Permission> permissions;
}
