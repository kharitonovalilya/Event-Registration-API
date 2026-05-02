package com.kharitonovalilya.event_registration_api.controller;

import com.kharitonovalilya.event_registration_api.dto.request.CreateEventRequest;
import com.kharitonovalilya.event_registration_api.dto.request.UpdateEventStatusRequest;
import com.kharitonovalilya.event_registration_api.dto.response.EventResponse;
import com.kharitonovalilya.event_registration_api.model.EventStatus;
import com.kharitonovalilya.event_registration_api.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@Valid @RequestBody CreateEventRequest request){
        return eventService.createEvent(request);
    }

    @GetMapping
    public List<EventResponse> getAllEvents(@RequestParam(required = false) EventStatus status){
        return eventService.getAllEvents(status);
    }

    @GetMapping("/with-seats")
    public List<EventResponse> getEventsWithAvailableSeats(){
        return eventService.getEventsWithAvailableSeats();
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable Long id){
        return eventService.getEventById(id);
    }

    @PatchMapping("/{id}/status")
    public EventResponse updateEventStatus(@Valid @RequestBody UpdateEventStatusRequest request, @PathVariable Long id){
        return eventService.updateEventStatus(id, request);
    }
}
