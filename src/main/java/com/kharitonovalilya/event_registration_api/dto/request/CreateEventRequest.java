package com.kharitonovalilya.event_registration_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class CreateEventRequest {
    @NotBlank(message = "Title must not be blank")
    private String title;

    private String description;

    @NotBlank(message = "Location must not be blank")
    private String location;

    @NotNull(message = "Start time must not be null")
    private LocalDateTime startsAt;

    @NotNull(message = "End time must not be null")
    private LocalDateTime endsAt;

    @NotNull(message = "Capacity must not be null")
    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    public CreateEventRequest(){}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
