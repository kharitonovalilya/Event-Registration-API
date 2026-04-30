package com.kharitonovalilya.event_registration_api.entity;

import com.kharitonovalilya.event_registration_api.model.EventStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime endsAt;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    protected Event(){

    }

    private Event(String title, String description, String location, LocalDateTime startsAt, LocalDateTime endsAt, Integer capacity){
        this.title = title;
        this.description = description;
        this.location = location;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.capacity = capacity;
        this.status = EventStatus.OPEN;
    }

    public static Event create(String title,
                               String description,
                               String location,
                               LocalDateTime startsAt,
                               LocalDateTime endsAt,
                               Integer capacity) {
        return new Event(title, description, location, startsAt, endsAt, capacity);
    }

    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getLocation(){
        return location;
    }

    public LocalDateTime getStartsAt(){
        return startsAt;
    }

    public LocalDateTime getEndsAt(){
        return endsAt;
    }

    public Integer getCapacity(){
        return capacity;
    }

    public EventStatus getStatus(){
        return status;
    }

    public void close() {
        this.status = EventStatus.CLOSED;
    }

    public void cancel() {
        this.status = EventStatus.CANCELLED;
    }

    public void reOpen() {
        this.status = EventStatus.OPEN;
    }
}
