package com.tima.dto;

import com.tima.model.Permission;
import com.tima.model.Role;
import com.tima.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrentUserResponse {
    private User user;
    private Role role;
    private List<Permission> permissions;
}
