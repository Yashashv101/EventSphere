package com.yash.eventsphere.dto.event;
import com.yash.eventsphere.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventListResponse {
    private UUID id;
    private String title;
    private String venue;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private EventStatus status;
    private Integer totalTicketTypes;
    private Integer totalAvailableTickets;
}
