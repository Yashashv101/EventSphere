package com.yash.eventsphere.dto.event;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTicketTypeRequest {
    @NotBlank(message="Ticket type name is required")
    @Size(min=2,max=100,message="Name must be between 2 and 100 characters")
    private String name;
    @Size(max=500,message="Description cannot exceed 500 characters")
    private String description;
    @NotNull(message="Price is required")
    @DecimalMin(value="0.00",message="Price cannot be negative")
    private Double price;
    @NotNull(message="Total quantity is required")
    @Min(value=1,message="Total quantity must be at least 1")
    private Integer totalQuantity;
}

