package com.spring.ai.demo.demo.Services.Impl;

import com.spring.ai.demo.demo.DTO.TicketCommand;
import com.spring.ai.demo.demo.Entities.Ticket;
import com.spring.ai.demo.demo.Enums.Status;
import com.spring.ai.demo.demo.Repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl {

    private final TicketRepository ticketRepository;

    //Create Ticket
    public Ticket createTicket(TicketCommand input) {
        Ticket ticket = new Ticket();
        ticket.setUserName(input.userName());
        ticket.setEmail(input.email());
        ticket.setSummary(input.summary());
        ticket.setPriority(input.priority());
        ticket.setStatus(Status.Open);
        log.info("Creating ticket for user: {}", input.userName());
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }

    public Ticket getTicketByUserName(String userName) {
        return ticketRepository.findByUserName(userName).orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket updateTicket(TicketCommand ticket, Long ticketId) {
        Ticket existingTicket = getTicketById(ticketId);
        if (existingTicket != null) {
            existingTicket.setSummary(ticket.summary());
            existingTicket.setPriority(ticket.priority());
            existingTicket.setStatus(ticket.status());
            existingTicket.setEmail(ticket.email());
            return ticketRepository.save(existingTicket);
        }
        return null;
    }

}
