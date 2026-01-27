package com.yash.eventsphere.dto.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEventRequest {
    @NotBlank(message="Event title is required")
    @Size(min=3,max=200,message="Title must be between 3 and 200 characters")
    private String title;
    @NotBlank(message="Venue is required")
    @Size(max=500,message="Venue cannot exceed 500 characters")
    private String venue;
    @Size(max=5000,message="Description cannot exceed 5000 characters")
    private String description;
    @NotNull(message="Start time is required")
    @Future(message="Start time must be in the future")
    private LocalDateTime startTime;
    @NotNull(message="End time is required")
    @Future(message="End time must be in the future")
    private LocalDateTime endTime;
}
