package com.kharitonovalilya.event_registration_api.service;

import com.kharitonovalilya.event_registration_api.dto.request.CreateEventRequest;
import com.kharitonovalilya.event_registration_api.dto.response.EventResponse;
import com.kharitonovalilya.event_registration_api.entity.Event;
import com.kharitonovalilya.event_registration_api.exception.BadRequestException;
import com.kharitonovalilya.event_registration_api.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
