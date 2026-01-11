package com.spring.ai.demo.demo.Entities;

import com.spring.ai.demo.demo.Enums.Priority;
import com.spring.ai.demo.demo.Enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "help_desk")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(nullable = false)
    private String summary;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Column(nullable = false, unique = true)
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(unique = true, nullable = false)
    private String email;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    // getters & setters
}
