package com.yash.eventsphere.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    String code;
    String message;
    LocalDateTime timestamp;
    String path;
    List<FieldError> errors;

    public ErrorResponse(String code,String message){
        this.code=code;
        this.message=message;
        this.timestamp=LocalDateTime.now();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FieldError{
        String field;
        String message;
        Object rejectedValue;
    }
}
