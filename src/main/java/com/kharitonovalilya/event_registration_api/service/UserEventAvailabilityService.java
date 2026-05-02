package com.kharitonovalilya.event_registration_api.service;

import com.kharitonovalilya.event_registration_api.dto.response.EventResponse;
import com.kharitonovalilya.event_registration_api.entity.Event;
import com.kharitonovalilya.event_registration_api.entity.User;
import com.kharitonovalilya.event_registration_api.exception.NotFoundException;
import com.kharitonovalilya.event_registration_api.model.EventStatus;
import com.kharitonovalilya.event_registration_api.model.RegistrationStatus;
import com.kharitonovalilya.event_registration_api.repository.EventRepository;
import com.kharitonovalilya.event_registration_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserEventAvailabilityService {
    private static final List<RegistrationStatus> ACTIVE_STATUSES = List.of(
            RegistrationStatus.CONFIRMED,
            RegistrationStatus.WAITLISTED
    );

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public UserEventAvailabilityService(UserRepository userRepository,
                                        EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getAvailableEventsForUser(Long userId){
        User user = findUserById(userId);
        List<Event> events = eventRepository.findAvailableEventsForUser(user, EventStatus.OPEN, ACTIVE_STATUSES);
        return events.stream().map(EventResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getAvailableSeatsWithSeatsForUser(Long userId){
        User user = findUserById(userId);
        List<Event> events = eventRepository.findAvailableSeatsWithSeatsForUser(user, EventStatus.OPEN, ACTIVE_STATUSES, RegistrationStatus.CONFIRMED);
        return events.stream().map(EventResponse::from).toList();
    }

    private User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
