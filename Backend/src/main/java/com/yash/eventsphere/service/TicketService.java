package com.yash.eventsphere.service;

import com.yash.eventsphere.dto.ticket.PurchaseTicketRequest;
import com.yash.eventsphere.dto.ticket.TicketResponse;
import com.yash.eventsphere.entity.*;
import com.yash.eventsphere.enums.EventStatus;
import com.yash.eventsphere.enums.TicketStatus;
import com.yash.eventsphere.enums.UserRole;
import com.yash.eventsphere.exception.ApiException;
import com.yash.eventsphere.mapper.TicketMapper;
import com.yash.eventsphere.repository.TicketRepository;
import com.yash.eventsphere.repository.TicketTypeRepository;
import com.yash.eventsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final UserRepository userRepository;
    private final QRCodeService qrCodeService;
    private final TicketMapper ticketMapper;

    @Transactional
    public TicketResponse purchaseTicket(PurchaseTicketRequest request, UUID attendeeId, String email) {
        log.info("Processing ticket purchase request for ticket type {} by attendee {}",
                request.getTicketTypeId(), attendeeId);
        TicketType ticketType = ticketTypeRepository.findByIdWithLock(request.getTicketTypeId())
                .orElseThrow(() -> ApiException.notFound("TicketType", request.getTicketTypeId()));
        Event event = ticketType.getEvent();
        if (event == null) {
            throw ApiException.notFound("Event for ticket type", request.getTicketTypeId());
        }
        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw ApiException.badRequest(
                    String.format("Cannot purchase tickets for event in %s status", event.getStatus()));
        }
        if (!ticketType.hasAvailableTickets()) {
            throw ApiException.conflict(
                    String.format("Ticket type '%s' is sold out", ticketType.getName()));
        }
        ticketType.decrementQuantity();
        ticketTypeRepository.save(ticketType);
        User attendee = getOrCreateAttendee(attendeeId, email);
        Ticket ticket = Ticket.builder()
                .ticketType(ticketType)
                .attendee(attendee)
                .status(TicketStatus.ACTIVE)
                .build();
        QRCode qrCode = qrCodeService.generateForTicket(ticket);
        ticket.setQrCode(qrCode);
        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Successfully purchased ticket {} for attendee {}",
                savedTicket.getId(), attendeeId);
        return ticketMapper.toResponse(savedTicket);
    }
    @Transactional(readOnly = true)
    public TicketResponse getTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findByIdWithDetails(ticketId)
                .orElseThrow(() -> ApiException.notFound("Ticket", ticketId));
        return ticketMapper.toResponse(ticket);
    }
    @Transactional(readOnly = true)
    public List<TicketResponse> getAttendeeTickets(UUID attendeeId) {
        List<Ticket> tickets = ticketRepository.findByAttendeeIdWithDetails(attendeeId);
        return tickets.stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public TicketResponse getTicketForAttendee(UUID ticketId, UUID attendeeId) {
        Ticket ticket = ticketRepository.findByIdWithDetails(ticketId)
                .orElseThrow(() -> ApiException.notFound("Ticket", ticketId));
        if (!ticket.getAttendee().getId().equals(attendeeId)) {
            throw ApiException.notFound("Ticket", ticketId);
        }
        return ticketMapper.toResponse(ticket);
    }
    private User getOrCreateAttendee(UUID userId, String email) {
        return userRepository.findById(userId)
                .orElseGet(() -> {
                    User user = User.builder()
                            .id(userId)
                            .email(email)
                            .role(UserRole.ATTENDEE)
                            .build();
                    return userRepository.save(user);
                });
    }
}
