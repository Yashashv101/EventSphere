package com.yash.eventsphere.dto.ticket;

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
public class ValidationResponse {
    private String result;
    private UUID ticketId;
    private String eventTitle;
    private String ticketTypeName;
    private String attendeeEmail;
    private LocalDateTime validatedAt;
    private String message;
}
