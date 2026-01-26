package com.yash.eventsphere.mapper;

import com.yash.eventsphere.dto.ticket.TicketResponse;
import com.yash.eventsphere.dto.ticket.ValidationResponse;
import com.yash.eventsphere.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    @Mapping(target = "eventTitle", source = "ticketType.event.title")
    @Mapping(target = "eventVenue", source = "ticketType.event.venue")
    @Mapping(target = "eventStartTime", source = "ticketType.event.startTime")
    @Mapping(target = "ticketTypeName", source = "ticketType.name")
    @Mapping(target = "price", source = "ticketType.price")
    @Mapping(target = "qrCodeValue", source = "qrCode.encodedValue")
    @Mapping(target = "qrCodeImageData", source = "qrCode.imageData")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "result", constant = "SUCCESS")
    @Mapping(target = "ticketId", source = "ticket.id")
    @Mapping(target = "eventTitle", source = "ticket.ticketType.event.title")
    @Mapping(target = "ticketTypeName", source = "ticket.ticketType.name")
    @Mapping(target = "attendeeEmail", source = "ticket.attendee.email")
    @Mapping(target = "message", constant = "Ticket validated successfully")
    ValidationResponse toValidationResponse(Ticket ticket);
}

