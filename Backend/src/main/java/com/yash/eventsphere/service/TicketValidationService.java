package com.yash.eventsphere.service;

import com.yash.eventsphere.dto.ticket.ValidateTicketRequest;
import com.yash.eventsphere.dto.ticket.ValidationResponse;
import com.yash.eventsphere.entity.Ticket;
import com.yash.eventsphere.entity.TicketValidation;
import com.yash.eventsphere.entity.User;
import com.yash.eventsphere.enums.UserRole;
import com.yash.eventsphere.exception.ApiException;
import com.yash.eventsphere.mapper.TicketMapper;
import com.yash.eventsphere.repository.TicketRepository;
import com.yash.eventsphere.repository.TicketValidationRepository;
import com.yash.eventsphere.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    @Transactional
    public ValidationResponse validateTicket(ValidateTicketRequest request, UUID staffId, String staffEmail) {
        log.info("Validating ticket with QR code by staff {}", staffId);
        Ticket ticket = ticketRepository.findByQrCodeEncodedValue(request.getQrCodeValue())
                .orElseThrow(() -> {
                    log.warn("Invalid QR code presented: {}", request.getQrCodeValue());
                    return ApiException.notFound("Ticket with QR code", request.getQrCodeValue());
                });
        if (ticket.isUsed()) {
            log.warn("Duplicate validation attempt for ticket {}", ticket.getId());
            throw ApiException.alreadyValidated(ticket.getId());
        }
        User staff = getOrCreateStaff(staffId, staffEmail);
        ticket.markAsUsed();
        ticketRepository.save(ticket);
        TicketValidation validation = TicketValidation.builder()
                .ticket(ticket)
                .validatedBy(staff)
                .validatedAt(LocalDateTime.now())
                .build();
        validationRepository.save(validation);
        log.info("Successfully validated ticket {} by staff {}", ticket.getId(), staffId);
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
    private User getOrCreateStaff(UUID userId, String email) {
        return userRepository.findById(userId)
                .orElseGet(() -> {
                    User user = User.builder()
                            .id(userId)
                            .email(email)
                            .role(UserRole.STAFF)
                            .build();
                    return userRepository.save(user);
                });
    }
}
