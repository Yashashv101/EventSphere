package com.yash.eventsphere.controller;

import com.yash.eventsphere.dto.ticket.*;
import com.yash.eventsphere.service.TicketService;
import com.yash.eventsphere.service.TicketValidationService;
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
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final TicketValidationService validationService;

    @PostMapping("/purchase")
    public ResponseEntity<TicketResponse> purchaseTicket(
            @Valid @RequestBody PurchaseTicketRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Email", defaultValue = "attendee@example.com") String email) {
        UUID attendeeId = parseUserId(userId);
        log.info("Processing ticket purchase for attendee: {}", attendeeId);
        TicketResponse response = ticketService.purchaseTicket(request, attendeeId, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/my-tickets")
    public ResponseEntity<List<TicketResponse>> getMyTickets(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        UUID attendeeId = parseUserId(userId);
        log.info("Fetching tickets for attendee: {}", attendeeId);
        List<TicketResponse> tickets = ticketService.getAttendeeTickets(attendeeId);
        return ResponseEntity.ok(tickets);
    }
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponse> validateTicket(
            @Valid @RequestBody ValidateTicketRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Email", defaultValue = "staff@example.com") String email) {
        UUID staffId = parseUserId(userId);
        log.info("Validating ticket by staff: {}", staffId);
        ValidationResponse response = validationService.validateTicket(request, staffId, email);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable UUID id) {
        log.info("Fetching ticket {}", id);
        TicketResponse response = ticketService.getTicket(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}/validation-status")
    public ResponseEntity<ValidationResponse> getValidationStatus(@PathVariable UUID id) {
        log.info("Fetching validation status for ticket {}", id);
        ValidationResponse response = validationService.getValidationStatus(id);
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
