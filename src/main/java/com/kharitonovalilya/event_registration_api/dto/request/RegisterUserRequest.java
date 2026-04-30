package com.kharitonovalilya.event_registration_api.dto.request;

import jakarta.validation.constraints.NotNull;

public class RegisterUserRequest {
    @NotNull(message = "Id must be not null")
    private Long userId;

    public RegisterUserRequest(){}

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }
}
