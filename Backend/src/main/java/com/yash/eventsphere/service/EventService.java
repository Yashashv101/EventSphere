package com.yash.eventsphere.service;

import com.yash.eventsphere.dto.event.*;
import com.yash.eventsphere.entity.Event;
import com.yash.eventsphere.entity.TicketType;
import com.yash.eventsphere.entity.User;
import com.yash.eventsphere.enums.EventStatus;
import com.yash.eventsphere.exception.ApiException;
import com.yash.eventsphere.mapper.EventMapper;
import com.yash.eventsphere.repository.EventRepository;
import com.yash.eventsphere.repository.TicketTypeRepository;
import com.yash.eventsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private EventRepository eventRepository;
    private TicketTypeRepository ticketTypeRepository;
    private UserRepository userRepository;
    private EventMapper eventMapper;

    @Transactional
    public EventResponse createEvent(CreateEventRequest request, UUID organizerId) {
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw ApiException.badRequest("End time must be after start time");
        }
        User organizer = getOrCreateUser(organizerId, request);
        Event event = eventMapper.toEntity(request);
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);
        Event savedEvent = eventRepository.save(event);
        log.info("Created event '{}' (ID: {}) by organizer {}",
                savedEvent.getTitle(), savedEvent.getId(), organizerId);
        return eventMapper.toResponse(savedEvent);
    }

    @Transactional
    public EventResponse publishEvent(UUID eventId, UUID organizerId) {
        Event event = eventRepository.findByIdWithTicketTypes(eventId)
                .orElseThrow(() -> ApiException.notFound("Event", eventId));
        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw ApiException.forbidden("You can only publish your own events");
        }
        if (event.getStatus() != EventStatus.DRAFT) {
            throw ApiException.badRequest(
                    String.format("Cannot publish event in %s status", event.getStatus()));
        }
        if (event.getTicketTypes() == null || event.getTicketTypes().isEmpty()) {
            throw ApiException.badRequest(
                    "Cannot publish event without at least one ticket type");
        }
        event.setStatus(EventStatus.PUBLISHED);
        Event savedEvent = eventRepository.save(event);
        log.info("Published event '{}' (ID: {})", savedEvent.getTitle(), eventId);
        return eventMapper.toResponse(savedEvent);
    }

    @Transactional
    public EventResponse closeEvent(UUID eventId, UUID organizerId) {
        Event event = eventRepository.findByIdWithTicketTypes(eventId)
                .orElseThrow(() -> ApiException.notFound("Event", eventId));
        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw ApiException.forbidden("You can only close your own events");
        }
        if (event.getStatus() == EventStatus.CLOSED) {
            throw ApiException.badRequest("Event is already closed");
        }
        event.setStatus(EventStatus.CLOSED);
        Event savedEvent = eventRepository.save(event);
        log.info("Closed event '{}' (ID: {})", savedEvent.getTitle(), eventId);
        return eventMapper.toResponse(savedEvent);
    }

    @Transactional
    public TicketTypeResponse addTicketType(UUID eventId, CreateTicketTypeRequest request, UUID organizerId) {
        Event event = eventRepository.findByIdWithOrganizer(eventId)
                .orElseThrow(() -> ApiException.notFound("Event", eventId));
        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw ApiException.forbidden("You can only add ticket types to your own events");
        }
        if (event.getStatus() != EventStatus.DRAFT) {
            throw ApiException.badRequest(
                    "Can only add ticket types to events in DRAFT status");
        }
        TicketType ticketType = eventMapper.toEntity(request);
        ticketType.setEvent(event);
        ticketType.setRemainingQuantity(request.getTotalQuantity());
        TicketType savedTicketType = ticketTypeRepository.save(ticketType);
        log.info("Added ticket type '{}' to event '{}'",
                savedTicketType.getName(), event.getTitle());
        return eventMapper.toResponse(savedTicketType);
    }

    @Transactional(readOnly = true)
    public List<EventListResponse> getPublishedEvents() {
        List<Event> events = eventRepository.findAllPublishedWithTicketTypes();
        return eventMapper.toListResponses(events);
    }

    @Transactional(readOnly = true)
    public List<EventListResponse> getOrganizerEvents(UUID organizerId) {
        List<Event> events = eventRepository.findByOrganizerId(organizerId);
        return eventMapper.toListResponses(events);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventDetails(UUID eventId) {
        Event event = eventRepository.findByIdWithTicketTypes(eventId)
                .orElseThrow(() -> ApiException.notFound("Event", eventId));
        return eventMapper.toResponse(event);
    }

    @Transactional(readOnly = true)
    public EventResponse getPublishedEventDetails(UUID eventId) {
        Event event = eventRepository.findByIdWithTicketTypes(eventId)
                .orElseThrow(() -> ApiException.notFound("Event", eventId));
        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw ApiException.notFound("Event", eventId);
        }
        return eventMapper.toResponse(event);
    }

    private User getOrCreateUser(UUID userId, CreateEventRequest request) {
        return userRepository.findById(userId)
                .orElseGet(() -> {
                    User user = User.builder()
                            .id(userId)
                            .email("organizer@example.com")
                            .role(com.yash.eventsphere.enums.UserRole.ORGANIZER)
                            .build();
                    return userRepository.save(user);
                });
    }
}
