package com.yash.eventsphere.controller;

import com.yash.eventsphere.dto.ticket.*;
import com.yash.eventsphere.entity.User;
import com.yash.eventsphere.service.TicketService;
import com.yash.eventsphere.service.TicketValidationService;
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
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final TicketValidationService validationService;

    @PostMapping("/purchase")
    public ResponseEntity<TicketResponse> purchaseTicket(
            @Valid @RequestBody PurchaseTicketRequest request,
            @AuthenticationPrincipal User user) {
        log.info("Processing ticket purchase for attendee: {}", user.getId());
        TicketResponse response = ticketService.purchaseTicket(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-tickets")
    public ResponseEntity<List<TicketResponse>> getMyTickets(
            @AuthenticationPrincipal User user) {
        log.info("Fetching tickets for attendee: {}", user.getId());
        List<TicketResponse> tickets = ticketService.getAttendeeTickets(user.getId());
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidationResponse> validateTicket(
            @Valid @RequestBody ValidateTicketRequest request,
            @AuthenticationPrincipal User user) {
        log.info("Validating ticket by staff: {}", user.getId());
        ValidationResponse response = validationService.validateTicket(request, user);
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
}
