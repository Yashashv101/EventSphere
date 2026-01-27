package com.yash.eventsphere.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }
    public HttpStatus getStatus() {
        return status;
    }
    public String getCode() {
        return code;
    }
    public static ApiException notFound(String resource, Object id) {
        return new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND",
                String.format("%s not found with identifier: %s", resource, id));
    }
    public static ApiException badRequest(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, "INVALID_OPERATION", message);
    }
    public static ApiException conflict(String message) {
        return new ApiException(HttpStatus.CONFLICT, "CONFLICT", message);
    }
    public static ApiException soldOut(String ticketType, int requested, int available) {
        return new ApiException(HttpStatus.CONFLICT, "SOLD_OUT",
                String.format("Cannot book %d tickets for '%s'. Only %d available.",
                        requested, ticketType, available));
    }
    public static ApiException alreadyValidated(Object ticketId) {
        return new ApiException(HttpStatus.CONFLICT, "ALREADY_VALIDATED",
                String.format("Ticket %s has already been validated", ticketId));
    }
    public static ApiException forbidden(String message) {
        return new ApiException(HttpStatus.FORBIDDEN, "FORBIDDEN", message);
    }
    public static ApiException forbidden() {
        return forbidden("You are not authorized to perform this action");
    }
}
