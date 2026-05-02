package com.kharitonovalilya.event_registration_api.service;

import com.kharitonovalilya.event_registration_api.dto.request.CreateEventRequest;
import com.kharitonovalilya.event_registration_api.dto.request.UpdateEventStatusRequest;
import com.kharitonovalilya.event_registration_api.dto.response.EventResponse;
import com.kharitonovalilya.event_registration_api.entity.Event;
import com.kharitonovalilya.event_registration_api.exception.BadRequestException;
import com.kharitonovalilya.event_registration_api.exception.NotFoundException;
import com.kharitonovalilya.event_registration_api.model.EventStatus;
import com.kharitonovalilya.event_registration_api.model.RegistrationStatus;
import com.kharitonovalilya.event_registration_api.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
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

        switch (request.getStatus()) {
            case OPEN -> event.reopen();
            case CLOSED -> event.close();
            case CANCELLED -> event.cancel();
            default -> throw new BadRequestException("Unsupported event status");
        }

        return EventResponse.from(event);
    }

    private Event findEventById(Long id){
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }
}
