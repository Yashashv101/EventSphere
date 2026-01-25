package com.yash.eventsphere.entity;

import com.yash.eventsphere.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    UUID id;
    @Column(nullable=false,unique=true)
    String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    UserRole role;
    @Column(name="created_at",nullable=false,updatable=false)
    LocalDateTime createdAt;
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
    }

}
