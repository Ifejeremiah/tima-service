package com.tima.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tima.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserLoginResponse {
    private String accessToken;
    private String id;
    private String email;

    public UserLoginResponse(User user) {
        update(user);
    }

    public void update(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
