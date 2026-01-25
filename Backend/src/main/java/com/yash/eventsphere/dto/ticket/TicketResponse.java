package com.yash.eventsphere.dto.ticket;
import com.yash.eventsphere.enums.TicketStatus;
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
public class TicketResponse {
    private UUID id;
    private String eventTitle;
    private String eventVenue;
    private LocalDateTime eventStartTime;
    private String ticketTypeName;
    private Double price;
    private TicketStatus status;
    private String qrCodeValue;
    private String qrCodeImageData;
    private LocalDateTime purchasedAt;
}
