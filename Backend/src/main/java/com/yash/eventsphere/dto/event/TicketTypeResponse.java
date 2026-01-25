package com.yash.eventsphere.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketTypeResponse {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer totalQuantity;
    private Integer remainingQuantity;
}
