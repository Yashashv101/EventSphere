package com.yash.eventsphere.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateTicketRequest {
    @NotBlank(message="QR cide value is required")
    private String qrCodeValue;
}
