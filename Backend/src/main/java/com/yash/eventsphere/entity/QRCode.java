package com.yash.eventsphere.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name="qr_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private Ticket ticket;
    @Column(name = "encoded_value", nullable = false, unique = true)
    private String encodedValue;
    @Column(name = "image_data", columnDefinition = "TEXT")
    private String imageData;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

