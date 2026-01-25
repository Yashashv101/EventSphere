package com.yash.eventsphere.entity;

import com.yash.eventsphere.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(nullable=false)
    String title;
    @Column(nullable=false)
    String venue;
    @Column(columnDefinition = "TEXT")
    String description;
    @Column(name="start_time",nullable=false)
    LocalDateTime startTime;
    @Column(name = "end_time",nullable=false)
    LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @Builder.Default
    EventStatus status=EventStatus.DRAFT;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="oranizer_id",nullable=false)
    User organizer;
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    List<TicketType> ticketType=new ArrayList<>();
    @Column(name="created-at", nullable = false,updatable = false)
    LocalDateTime createdAt;
    @Column(name="updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
        updatedAt=LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt=LocalDateTime.now();
    }

    public boolean isOpenForSales(){
        return status==EventStatus.PUBLISHED;
    }

    public void addTicketType(TicketType ticketType){
        this.ticketType.add(ticketType);
        ticketType.setEvent(this);
    }
}
