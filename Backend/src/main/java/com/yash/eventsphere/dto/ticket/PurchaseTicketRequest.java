package com.yash.eventsphere.dto.ticket;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseTicketRequest {
    @NotNull(message="Ticket type ID is required")
    private UUID ticketTypeId;
}

