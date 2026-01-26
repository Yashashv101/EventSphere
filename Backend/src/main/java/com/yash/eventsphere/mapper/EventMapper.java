package com.yash.eventsphere.mapper;

import com.yash.eventsphere.dto.event.*;
import com.yash.eventsphere.entity.Event;
import com.yash.eventsphere.entity.TicketType;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Event and TicketType entities.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    /**
     * Maps CreateEventRequest to Event entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "ticketTypes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Event toEntity(CreateEventRequest request);

    /**
     * Maps Event entity to EventResponse DTO.
     */
    @Mapping(target = "organizerEmail", source = "organizer.email")
    @Mapping(target = "ticketTypes", source = "ticketTypes")
    EventResponse toResponse(Event event);

    /**
     * Maps Event entity to EventListResponse DTO.
     */
    @Mapping(target = "totalTicketTypes", expression = "java(event.getTicketTypes() != null ? event.getTicketTypes().size() : 0)")
    @Mapping(target = "totalAvailableTickets", expression = "java(calculateTotalAvailableTickets(event))")
    EventListResponse toListResponse(Event event);

    /**
     * Maps list of events to list responses.
     */
    List<EventListResponse> toListResponses(List<Event> events);

    /**
     * Maps CreateTicketTypeRequest to TicketType entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "remainingQuantity", source = "totalQuantity")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TicketType toEntity(CreateTicketTypeRequest request);

    /**
     * Maps TicketType entity to TicketTypeResponse DTO.
     */
    TicketTypeResponse toResponse(TicketType ticketType);

    /**
     * Maps list of ticket types to list of responses.
     */
    List<TicketTypeResponse> toTicketTypeResponses(List<TicketType> ticketTypes);

    /**
     * Helper method to calculate total available tickets across all types.
     */
    default Integer calculateTotalAvailableTickets(Event event) {
        if (event.getTicketTypes() == null || event.getTicketTypes().isEmpty()) {
            return 0;
        }
        return event.getTicketTypes().stream()
                .mapToInt(tt -> tt.getRemainingQuantity() != null ? tt.getRemainingQuantity() : 0)
                .sum();
    }
}
