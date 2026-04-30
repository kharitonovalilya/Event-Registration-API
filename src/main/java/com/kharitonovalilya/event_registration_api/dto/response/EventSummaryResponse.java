package com.kharitonovalilya.event_registration_api.dto.response;

import com.kharitonovalilya.event_registration_api.model.EventStatus;

public class EventSummaryResponse {
    private final Long eventId;
    private final String title;
    private final Integer capacity;
    private final long confirmedCount;
    private final long waitlistedCount;
    private final long availableSeats;
    private final EventStatus status;

    public EventSummaryResponse(Long eventId, String title, Integer capacity,
                                long confirmedCount, long waitlistedCount,
                                EventStatus status) {
        this.eventId = eventId;
        this.title = title;
        this.capacity = capacity;
        this.confirmedCount = confirmedCount;
        this.waitlistedCount = waitlistedCount;
        this.availableSeats = capacity - confirmedCount;
        this.status = status;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public long getConfirmedCount() {
        return confirmedCount;
    }

    public long getWaitlistedCount() {
        return waitlistedCount;
    }

    public long getAvailableSeats() {
        return availableSeats;
    }

    public EventStatus getStatus() {
        return status;
    }
}
