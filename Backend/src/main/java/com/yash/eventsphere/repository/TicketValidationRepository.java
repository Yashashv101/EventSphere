package com.yash.eventsphere.repository;

import com.yash.eventsphere.entity.TicketValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {
    Optional<TicketValidation> findByTicketId(UUID ticketId);
    boolean existsByTicketId(UUID ticketId);
}
