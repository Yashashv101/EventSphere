package com.yash.eventsphere.repository;

import com.yash.eventsphere.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByAttendeeId(UUID attendeeId);
    @Query("SELECT t FROM Ticket t " +
            "LEFT JOIN FETCH t.qrCode q " +
            "LEFT JOIN FETCH t.ticketType tt " +
            "LEFT JOIN FETCH tt.event e " +
            "LEFT JOIN FETCH t.attendee a " +
            "WHERE q.encodedValue = :qrCodeValue")
    Optional<Ticket> findByQrCodeEncodedValue(@Param("qrCodeValue") String qrCodeValue);
    @Query("SELECT t FROM Ticket t " +
            "LEFT JOIN FETCH t.qrCode " +
            "LEFT JOIN FETCH t.ticketType tt " +
            "LEFT JOIN FETCH tt.event " +
            "LEFT JOIN FETCH t.attendee " +
            "LEFT JOIN FETCH t.validation v " +
            "LEFT JOIN FETCH v.validatedBy " +
            "WHERE t.id = :id")
    Optional<Ticket> findByIdWithDetails(@Param("id") UUID id);
    @Query("SELECT t FROM Ticket t " +
            "LEFT JOIN FETCH t.qrCode " +
            "LEFT JOIN FETCH t.ticketType tt " +
            "LEFT JOIN FETCH tt.event " +
            "WHERE t.attendee.id = :attendeeId")
    List<Ticket> findByAttendeeIdWithDetails(@Param("attendeeId") UUID attendeeId);
}
