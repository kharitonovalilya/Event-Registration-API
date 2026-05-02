package com.kharitonovalilya.event_registration_api.controller;

import com.kharitonovalilya.event_registration_api.dto.response.EventResponse;
import com.kharitonovalilya.event_registration_api.service.UserEventAvailabilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/events")
public class UserEventAvailabilityController {
    private final UserEventAvailabilityService userEventAvailabilityService;

    public UserEventAvailabilityController(UserEventAvailabilityService userEventAvailabilityService){
        this.userEventAvailabilityService = userEventAvailabilityService;
    }

    @GetMapping("/available")
    public List<EventResponse> getAvailableSeatsForUser(@PathVariable Long userId){
        return userEventAvailabilityService.getAvailableEventsForUser(userId);
    }

    @GetMapping("/available/with-seats")
    public List<EventResponse> getAvailableSeatsWithSeatsForUser(@PathVariable Long userId){
        return userEventAvailabilityService.getAvailableSeatsWithSeatsForUser(userId);
    }
}
