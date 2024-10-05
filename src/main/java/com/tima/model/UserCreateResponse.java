package com.tima.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class UserCreateResponse {
    private String id;
    private String email;

    public UserCreateResponse(User user) {
        update(user);
    }

    public void update(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
