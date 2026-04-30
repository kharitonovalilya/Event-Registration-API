package com.kharitonovalilya.event_registration_api.entity;

import com.kharitonovalilya.event_registration_api.model.RegistrationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    private LocalDateTime cancelledAt;

    protected Registration(){

    }

    private Registration(Event event, User user, RegistrationStatus status){
        this.event = event;
        this.user = user;
        this.status = status;
        this.registeredAt = LocalDateTime.now();
    }

    public static Registration confirmed(Event event, User user){
        return new Registration(event, user, RegistrationStatus.CONFIRMED);
    }

    public static Registration waitlisted(Event event, User user){
        return new Registration(event, user, RegistrationStatus.WAITLISTED);
    }

    public Long getId(){
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public RegistrationStatus getStatus(){
        return status;
    }

    public void cancel(){
        this.status = RegistrationStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public void confirm(){
        this.status = RegistrationStatus.CONFIRMED;
    }
}
