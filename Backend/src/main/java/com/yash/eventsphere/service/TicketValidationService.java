package com.yash.eventsphere.service;

import com.yash.eventsphere.dto.ticket.ValidateTicketRequest;
import com.yash.eventsphere.dto.ticket.ValidationResponse;
import com.yash.eventsphere.entity.Ticket;
import com.yash.eventsphere.entity.TicketValidation;
import com.yash.eventsphere.entity.User;
import com.yash.eventsphere.exception.ApiException;
import com.yash.eventsphere.mapper.TicketMapper;
import com.yash.eventsphere.repository.TicketRepository;
import com.yash.eventsphere.repository.TicketValidationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketValidationService {
    private final TicketRepository ticketRepository;
    private final TicketValidationRepository validationRepository;
    private final TicketMapper ticketMapper;

    @Transactional
    public ValidationResponse validateTicket(ValidateTicketRequest request, User staff) {
        log.info("Validating ticket with QR code by staff {}", staff.getId());
        Ticket ticket = ticketRepository.findByQrCodeEncodedValue(request.getQrCodeValue())
                .orElseThrow(() -> {
                    log.warn("Invalid QR code presented: {}", request.getQrCodeValue());
                    return ApiException.notFound("Ticket with QR code", request.getQrCodeValue());
                });
        if (ticket.isUsed()) {
            log.warn("Duplicate validation attempt for ticket {}", ticket.getId());
            throw ApiException.alreadyValidated(ticket.getId());
        }
        ticket.markAsUsed();
        ticketRepository.save(ticket);
        TicketValidation validation = TicketValidation.builder()
                .ticket(ticket)
                .validatedBy(staff)
                .validatedAt(LocalDateTime.now())
                .build();
        validationRepository.save(validation);
        log.info("Successfully validated ticket {} by staff {}", ticket.getId(), staff.getId());
        ValidationResponse response = ticketMapper.toValidationResponse(ticket);
        response.setValidatedAt(validation.getValidatedAt());
        return response;
    }

    @Transactional(readOnly = true)
    public ValidationResponse getValidationStatus(UUID ticketId) {
        Ticket ticket = ticketRepository.findByIdWithDetails(ticketId)
                .orElseThrow(() -> ApiException.notFound("Ticket", ticketId));
        ValidationResponse response = ValidationResponse.builder()
                .ticketId(ticket.getId())
                .eventTitle(ticket.getTicketType().getEvent().getTitle())
                .ticketTypeName(ticket.getTicketType().getName())
                .attendeeEmail(ticket.getAttendee().getEmail())
                .build();
        if (ticket.isUsed() && ticket.getValidation() != null) {
            response.setResult("VALIDATED");
            response.setValidatedAt(ticket.getValidation().getValidatedAt());
            response.setMessage("Ticket has been validated");
        } else {
            response.setResult("ACTIVE");
            response.setMessage("Ticket is active and not yet validated");
        }
        return response;
    }
}
