package com.yash.eventsphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="ticket_types")
public class TicketType {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    UUID id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="event_id",nullable = false)
    Event event;
    @Column(nullable=false)
    String name;
    @Column(columnDefinition="TEXT")
    String description;
    @Column(nullable=false,precision=10,scale=2)
    Double price;
    @Column(name="total_quantity",nullable=false)
    Integer totalQuantity;
    @Column(name="total_quantity",nullable=false)
    Integer remainingQuantity;
    @Version
    Long version;
    @Column(name="created_at",nullable=false,updatable=false)
    LocalDateTime createdAt;

    public boolean hasAvailableTickets(){
        return remainingQuantity!=null && remainingQuantity>0;
    }

    public void decrementQuantity(){
        if(!hasAvailableTickets()){
            throw new IllegalStateException("TicketType has no available tickets");
        }
        this.remainingQuantity--;
    }
}
