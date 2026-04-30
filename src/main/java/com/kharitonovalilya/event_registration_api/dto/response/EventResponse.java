package com.kharitonovalilya.event_registration_api.dto.response;

import com.kharitonovalilya.event_registration_api.entity.Event;
import com.kharitonovalilya.event_registration_api.model.EventStatus;

import java.time.LocalDateTime;

public class EventResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final String location;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final Integer capacity;
    private final EventStatus status;

    public EventResponse(Long id, String title, String description, String location,
                         LocalDateTime startsAt, LocalDateTime endsAt,
                         Integer capacity, EventStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.capacity = capacity;
        this.status = status;
    }

    public static EventResponse from(Event event){
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getStartsAt(),
                event.getEndsAt(),
                event.getCapacity(),
                event.getStatus()
        );
    }

    public Long getId() {
        return id;
    }

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

    public EventStatus getStatus() {
        return status;
    }
}
