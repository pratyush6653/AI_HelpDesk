package com.spring.ai.demo.demo.DTO;

import com.spring.ai.demo.demo.Enums.Priority;
import com.spring.ai.demo.demo.Enums.Status;

import java.time.LocalDateTime;

public record TicketView(Long id,
                         String userName,
                         String email,
                         String summary,
                         Status status,
                         Priority priority,
                         LocalDateTime createdAt) {
}
