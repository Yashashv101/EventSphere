package com.yash.eventsphere.mapper;

import com.yash.eventsphere.dto.event.*;
import com.yash.eventsphere.entity.Event;
import com.yash.eventsphere.entity.TicketType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "venue", source = "venue")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    Event toEntity(CreateEventRequest request);

    @Mapping(target = "organizerEmail", source = "organizer.email")
    @Mapping(target = "ticketTypes", source = "ticketTypes")
    EventResponse toResponse(Event event);

    @Mapping(target = "totalTicketTypes", expression = "java(event.getTicketTypes() != null ? event.getTicketTypes().size() : 0)")
    @Mapping(target = "totalAvailableTickets", expression = "java(calculateTotalAvailableTickets(event))")
    EventListResponse toListResponse(Event event);

    List<EventListResponse> toListResponses(List<Event> events);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "remainingQuantity", source = "totalQuantity")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TicketType toEntity(CreateTicketTypeRequest request);

    TicketTypeResponse toResponse(TicketType ticketType);
    List<TicketTypeResponse> toTicketTypeResponses(List<TicketType> ticketTypes);

    default Integer calculateTotalAvailableTickets(Event event) {
        if (event.getTicketTypes() == null || event.getTicketTypes().isEmpty()) {
            return 0;
        }
        return event.getTicketTypes().stream()
                .mapToInt(tt -> tt.getRemainingQuantity() != null ? tt.getRemainingQuantity() : 0)
                .sum();
    }
}
