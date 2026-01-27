package com.yash.eventsphere.controller;

import com.yash.eventsphere.dto.event.*;
import com.yash.eventsphere.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        UUID organizerId = parseUserId(userId);
        log.info("Creating event for organizer: {}", organizerId);
        EventResponse response = eventService.createEvent(request, organizerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/{id}/publish")
    public ResponseEntity<EventResponse> publishEvent(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        UUID organizerId = parseUserId(userId);
        log.info("Publishing event {} by organizer {}", id, organizerId);
        EventResponse response = eventService.publishEvent(id, organizerId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/close")
    public ResponseEntity<EventResponse> closeEvent(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        UUID organizerId = parseUserId(userId);
        log.info("Closing event {} by organizer {}", id, organizerId);
        EventResponse response = eventService.closeEvent(id, organizerId);
        return ResponseEntity.ok(response);
    }

    /**
     * Adds a ticket type to an event.
     */
    @PostMapping("/{id}/ticket-types")
    public ResponseEntity<TicketTypeResponse> addTicketType(
            @PathVariable UUID id,
            @Valid @RequestBody CreateTicketTypeRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        UUID organizerId = parseUserId(userId);
        log.info("Adding ticket type to event {} by organizer {}", id, organizerId);
        TicketTypeResponse response = eventService.addTicketType(id, request, organizerId);
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
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        UUID organizerId = parseUserId(userId);
        log.info("Fetching events for organizer {}", organizerId);
        List<EventListResponse> events = eventService.getOrganizerEvents(organizerId);
        return ResponseEntity.ok(events);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID id) {
        log.info("Fetching event details for {}", id);
        EventResponse response = eventService.getEventDetails(id);
        return ResponseEntity.ok(response);
    }
    private UUID parseUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            return UUID.randomUUID();
        }
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            return UUID.randomUUID();
        }
    }
}

