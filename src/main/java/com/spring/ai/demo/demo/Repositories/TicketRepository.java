package com.spring.ai.demo.demo.Repositories;

import com.spring.ai.demo.demo.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByUserName(String userName);

    Optional<Ticket> findTopByEmailOrderByCreatedAtDesc(String email);
}
