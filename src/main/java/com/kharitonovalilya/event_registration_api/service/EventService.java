package com.kharitonovalilya.event_registration_api.service;

import com.kharitonovalilya.event_registration_api.dto.request.CreateEventRequest;
import com.kharitonovalilya.event_registration_api.dto.request.UpdateEventStatusRequest;
import com.kharitonovalilya.event_registration_api.dto.response.EventResponse;
import com.kharitonovalilya.event_registration_api.entity.Event;
import com.kharitonovalilya.event_registration_api.entity.Registration;
import com.kharitonovalilya.event_registration_api.exception.BadRequestException;
import com.kharitonovalilya.event_registration_api.exception.NotFoundException;
import com.kharitonovalilya.event_registration_api.model.EventStatus;
import com.kharitonovalilya.event_registration_api.model.RegistrationStatus;
import com.kharitonovalilya.event_registration_api.repository.EventRepository;
import com.kharitonovalilya.event_registration_api.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private static final List<RegistrationStatus> ACTIVE_STATUSES = List.of(
            RegistrationStatus.CONFIRMED,
            RegistrationStatus.WAITLISTED
    );

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventService(EventRepository eventRepository, RegistrationRepository registrationRepository){
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public EventResponse createEvent(CreateEventRequest request){
        if(!request.getStartsAt().isBefore(request.getEndsAt())){
            throw new BadRequestException("Start time must be before end time");
        }

        Event event = Event.create(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getStartsAt(),
                request.getEndsAt(),
                request.getCapacity()
        );

        validateEventDates(event.getStartsAt(), event.getEndsAt());

        Event savedEvent = eventRepository.save(event);
        return EventResponse.from(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents(EventStatus status){
        List<Event> events;

        if(status == null){
            events = eventRepository.findAll();
        }
        else {
            events = eventRepository.findByStatus(status);
        }

        List<EventResponse> responses = new ArrayList<>();
        for(Event event : events){
            responses.add(EventResponse.from(event));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getEventsWithAvailableSeats(){
        return eventRepository.findEventsWithAvailableSeats(
                EventStatus.OPEN,
                RegistrationStatus.CONFIRMED
        ).stream().map(EventResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id){
        Event event = findEventById(id);
        return EventResponse.from(event);
    }

    @Transactional
    public EventResponse updateEventStatus(Long id, UpdateEventStatusRequest request){
        Event event = findEventById(id);

        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new BadRequestException("Cancelled event cannot be reopened or changed");
        }

        switch (request.getStatus()) {
            case OPEN:
                ensureEventCanBeOpened(event);
                event.reopen();
                break;
            case CLOSED:
                event.close();
                break;
            case CANCELLED:
                cancelEvent(event);
                break;
            default:
                throw new BadRequestException("Unsupported event status");
        }
        return EventResponse.from(event);
    }

    private Event findEventById(Long id){
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    private void cancelEvent(Event event){
        event.cancel();
        List<Registration> activeRegistrations = registrationRepository.findByEventAndStatusIn(event, ACTIVE_STATUSES);
        for(Registration registration : activeRegistrations){
            registration.cancel();
        }
    }

    private void ensureEventCanBeOpened(Event event){
        LocalDateTime now = LocalDateTime.now();
        if (!event.getStartsAt().isAfter(now)) {
            throw new BadRequestException("Cannot open registration for an event that has already started or passed");
        }
    }

    private void validateEventDates(LocalDateTime startsAt, LocalDateTime endsAt) {
        LocalDateTime now = LocalDateTime.now();

        if (!startsAt.isAfter(now)) {
            throw new BadRequestException("Event start time must be in the future");
        }

        if (!endsAt.isAfter(startsAt)) {
            throw new BadRequestException("Event end time must be after start time");
        }
    }
}
