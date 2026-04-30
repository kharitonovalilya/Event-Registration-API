package com.kharitonovalilya.event_registration_api.dto.response;

import com.kharitonovalilya.event_registration_api.entity.Registration;
import com.kharitonovalilya.event_registration_api.model.RegistrationStatus;

import java.time.LocalDateTime;

public class RegistrationResponse {
    private final Long id;
    private final Long eventId;
    private final Long userId;
    private final String userName;
    private final RegistrationStatus status;
    private final LocalDateTime registeredAt;
    private final LocalDateTime cancelledAt;

    public RegistrationResponse(Long id, Long eventId, Long userId, String userName,
                                RegistrationStatus status,
                                LocalDateTime registeredAt,
                                LocalDateTime cancelledAt) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.registeredAt = registeredAt;
        this.cancelledAt = cancelledAt;
    }

    public static RegistrationResponse from(Registration registration) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getEvent().getId(),
                registration.getUser().getId(),
                registration.getUser().getName(),
                registration.getStatus(),
                registration.getRegisteredAt(),
                registration.getCancelledAt()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }
}
