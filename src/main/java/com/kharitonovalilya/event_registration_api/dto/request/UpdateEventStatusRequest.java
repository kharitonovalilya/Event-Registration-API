package com.kharitonovalilya.event_registration_api.dto.request;

import com.kharitonovalilya.event_registration_api.model.EventStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateEventStatusRequest {
    @NotNull(message = "Status must not be null")
    private EventStatus status;

    public UpdateEventStatusRequest(){}

    public EventStatus getStatus(){
        return status;
    }

    public void setStatus(EventStatus status){
        this.status = status;
    }
}
