package com.yash.eventsphere.controller;

import com.yash.eventsphere.dto.event.*;
import com.yash.eventsphere.entity.User;
import com.yash.eventsphere.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request,
            @AuthenticationPrincipal User user) {
        log.info("Creating event for organizer: {}", user.getId());
        EventResponse response = eventService.createEvent(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<EventResponse> publishEvent(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        log.info("Publishing event {} by organizer {}", id, user.getId());
        EventResponse response = eventService.publishEvent(id, user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<EventResponse> closeEvent(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        log.info("Closing event {} by organizer {}", id, user.getId());
        EventResponse response = eventService.closeEvent(id, user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/ticket-types")
    public ResponseEntity<TicketTypeResponse> addTicketType(
            @PathVariable UUID id,
            @Valid @RequestBody CreateTicketTypeRequest request,
            @AuthenticationPrincipal User user) {
        log.info("Adding ticket type to event {} by organizer {}", id, user.getId());
        TicketTypeResponse response = eventService.addTicketType(id, request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventListResponse>> getPublishedEvents() {
        log.info("Fetching published events");
        List<EventListResponse> events = eventService.getPublishedEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/my-events")
    public ResponseEntity<List<EventListResponse>> getMyEvents(
            @AuthenticationPrincipal User user) {
        log.info("Fetching events for organizer {}", user.getId());
        List<EventListResponse> events = eventService.getOrganizerEvents(user.getId());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID id) {
        log.info("Fetching event details for {}", id);
        EventResponse response = eventService.getEventDetails(id);
        return ResponseEntity.ok(response);
    }
}
