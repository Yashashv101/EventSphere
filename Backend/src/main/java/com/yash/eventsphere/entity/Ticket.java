package com.yash.eventsphere.entity;
import com.yash.eventsphere.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendee_id", nullable = false)
    private User attendee;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TicketStatus status = TicketStatus.ACTIVE;
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private QRCode qrCode;
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private TicketValidation validation;
    @Column(name = "purchased_at", nullable = false, updatable = false)
    private LocalDateTime purchasedAt;

    @PrePersist
    protected void onCreate() {
        purchasedAt = LocalDateTime.now();
    }
    public boolean isUsed() {
        return status == TicketStatus.USED;
    }
    public boolean canBeValidated() {
        return status == TicketStatus.ACTIVE;
    }
    public void markAsUsed() {
        this.status = TicketStatus.USED;
    }
    public void setQrCode(QRCode qrCode) {
        this.qrCode = qrCode;
        if (qrCode != null) {
            qrCode.setTicket(this);
        }
    }
}

