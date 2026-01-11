package com.spring.ai.demo.demo.Services.Impl;

import com.spring.ai.demo.demo.Entities.Ticket;
import com.spring.ai.demo.demo.Repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl {

    private final TicketRepository ticketRepository;

    //Create Ticket
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }

    public Ticket getTicketByUserName(String userName) {
        return ticketRepository.findByUserName(userName).orElse(null);
    }

    public Ticket updateTicket(Ticket ticket, Long ticketId) {
        Ticket existingTicket = getTicketById(ticketId);
        if (existingTicket != null) {
            existingTicket.setSummary(ticket.getSummary());
            existingTicket.setPriorty(ticket.getPriorty());
            existingTicket.setStatus(ticket.getStatus());
            existingTicket.setEmail(ticket.getEmail());
            return ticketRepository.save(existingTicket);
        }
        return null;
    }

}
