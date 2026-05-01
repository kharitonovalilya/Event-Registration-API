package com.kharitonovalilya.event_registration_api.controller;

import com.kharitonovalilya.event_registration_api.dto.request.RegisterUserRequest;
import com.kharitonovalilya.event_registration_api.dto.response.EventSummaryResponse;
import com.kharitonovalilya.event_registration_api.dto.response.RegistrationResponse;
import com.kharitonovalilya.event_registration_api.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    @PostMapping("/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse registerUser(@PathVariable Long eventId, @Valid @RequestBody RegisterUserRequest request){
        return registrationService.registerUser(eventId, request);
    }

    @GetMapping("/registrations")
    public List<RegistrationResponse> getRegistrationsByEvent(@PathVariable Long eventId){
        return registrationService.getRegistrationsByEvent(eventId);
    }

    @DeleteMapping("/registrations/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelRegistration(@PathVariable Long eventId, @PathVariable Long userId){
        registrationService.cancelRegistration(eventId, userId);
    }

    @GetMapping("/summary")
    public EventSummaryResponse getEventSummary(@PathVariable Long eventId){
        return registrationService.getEventSummary(eventId);
    }
}
