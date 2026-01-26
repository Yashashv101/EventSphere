package com.yash.eventsphere.repository;

import com.yash.eventsphere.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, UUID> {
    Optional<QRCode> findByEncodedValue(String encodedValue);
    Optional<QRCode> findByTicketId(UUID ticketId);
    boolean existsByEncodedValue(String encodedValue);
}

