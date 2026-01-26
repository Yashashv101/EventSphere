package com.yash.eventsphere.repository;

import com.yash.eventsphere.entity.Event;
import com.yash.eventsphere.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByStatus(EventStatus status);
    default List<Event> findPublishedEvents() {
        return findByStatus(EventStatus.PUBLISHED);
    }
    List<Event> findByOrganizerId(UUID organizerId);
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.ticketTypes WHERE e.id = :id")
    Optional<Event> findByIdWithTicketTypes(@Param("id") UUID id);
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.organizer WHERE e.id = :id")
    Optional<Event> findByIdWithOrganizer(@Param("id") UUID id);
    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.ticketTypes WHERE e.status = 'PUBLISHED'")
    List<Event> findAllPublishedWithTicketTypes();
}
