package com.kharitonovalilya.event_registration_api.service;

import com.kharitonovalilya.event_registration_api.dto.request.RegisterUserRequest;
import com.kharitonovalilya.event_registration_api.dto.response.EventSummaryResponse;
import com.kharitonovalilya.event_registration_api.dto.response.RegistrationResponse;
import com.kharitonovalilya.event_registration_api.entity.Event;
import com.kharitonovalilya.event_registration_api.entity.Registration;
import com.kharitonovalilya.event_registration_api.entity.User;
import com.kharitonovalilya.event_registration_api.exception.BadRequestException;
import com.kharitonovalilya.event_registration_api.exception.ConflictException;
import com.kharitonovalilya.event_registration_api.exception.NotFoundException;
import com.kharitonovalilya.event_registration_api.model.EventStatus;
import com.kharitonovalilya.event_registration_api.model.RegistrationStatus;
import com.kharitonovalilya.event_registration_api.repository.EventRepository;
import com.kharitonovalilya.event_registration_api.repository.RegistrationRepository;
import com.kharitonovalilya.event_registration_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationService {
    private static final List<RegistrationStatus> ACTIVE_STATUSES = List.of(
            RegistrationStatus.CONFIRMED,
            RegistrationStatus.WAITLISTED
    );

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RegistrationService(RegistrationRepository registrationRepository, EventRepository eventRepository, UserRepository userRepository){
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RegistrationResponse registerUser(Long eventId, RegisterUserRequest request){
        Event event = findEventByIdForUpdate(eventId);
        User user = findUserById(request.getUserId());

        if(event.getStatus() != EventStatus.OPEN){
            throw new BadRequestException("Event is not open for registration");
        }

        if(registrationRepository.existsByEventAndUserAndStatusIn(event, user, ACTIVE_STATUSES)){
            throw new ConflictException("User is already registered for this event");
        }

        ensureUserHasNoOverlappingRegistration(user, event);

        long confirmedCount = registrationRepository.countByEventAndStatus(event, RegistrationStatus.CONFIRMED);
        Registration registration;

        if(confirmedCount < event.getCapacity()){
            registration = Registration.confirmed(event, user);
        }
        else{
            registration = Registration.waitlisted(event, user);
        }

        Registration savedRegistration = registrationRepository.save(registration);
        return RegistrationResponse.from(savedRegistration);
    }

    @Transactional(readOnly = true)
    public List<RegistrationResponse> getRegistrationsByEvent(Long eventId){
        Event event = findEventById(eventId);

        List<Registration> registrations = registrationRepository.findByEvent(event);
        List<RegistrationResponse> responses = new ArrayList<>();

        for(Registration registration : registrations){
            responses.add(RegistrationResponse.from(registration));
        }

        return responses;
    }

    @Transactional
    public void cancelRegistration(Long eventId, Long userId){
        Event event = findEventByIdForUpdate(eventId);
        User user = findUserById(userId);

        Registration registration = registrationRepository
                .findByEventAndUserAndStatusIn(event, user, ACTIVE_STATUSES)
                .orElseThrow(() -> new BadRequestException("Active registration is not found"));

        boolean wasConfirmed = registration.getStatus() == RegistrationStatus.CONFIRMED;
        registration.cancel();
        if(wasConfirmed){
            registrationRepository.findFirstByEventAndStatusOrderByRegisteredAtAsc(
                    event,
                    RegistrationStatus.WAITLISTED
            ).ifPresent(Registration::confirm);
        }
    }

    @Transactional(readOnly = true)
    public EventSummaryResponse getEventSummary(Long eventId){
        Event event = findEventById(eventId);

        long confirmedCount = registrationRepository.countByEventAndStatus(event, RegistrationStatus.CONFIRMED);
        long waitlistedCount = registrationRepository.countByEventAndStatus(event, RegistrationStatus.WAITLISTED);

        return new EventSummaryResponse(
                event.getId(),
                event.getTitle(),
                event.getCapacity(),
                confirmedCount,
                waitlistedCount,
                event.getStatus()
        );
    }

    private Event findEventById(Long eventId){
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    private User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Event findEventByIdForUpdate(Long eventId){
        return eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    private void ensureUserHasNoOverlappingRegistration(User user, Event targetEvent){
        List<Registration> activeRegistrations = registrationRepository.findByUserAndStatusIn(user, ACTIVE_STATUSES);
        for(Registration registration : activeRegistrations){
            Event existingEvent = registration.getEvent();

            if(eventsOverlap(existingEvent, targetEvent)){
                throw new ConflictException("User already has an active registration for overlapping event");
            }
        }
    }

    private boolean eventsOverlap(Event firstEvent, Event secondEvent){
        return (firstEvent.getStartsAt().isBefore(secondEvent.getEndsAt())) && (secondEvent.getStartsAt().isBefore(firstEvent.getEndsAt()));
    }
}