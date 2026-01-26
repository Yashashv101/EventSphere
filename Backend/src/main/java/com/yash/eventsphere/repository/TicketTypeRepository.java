package com.yash.eventsphere.repository;

import com.yash.eventsphere.entity.TicketType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {
    List<TicketType> findByEventId(UUID eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tt FROM TicketType tt WHERE tt.id = :id")
    Optional<TicketType> findByIdWithLock(@Param("id") UUID id);

    @Query("SELECT tt FROM TicketType tt LEFT JOIN FETCH tt.event WHERE tt.id = :id")
    Optional<TicketType> findByIdWithEvent(@Param("id") UUID id);
}

