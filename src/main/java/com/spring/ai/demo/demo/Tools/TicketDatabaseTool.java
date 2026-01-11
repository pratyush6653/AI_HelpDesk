package com.spring.ai.demo.demo.Tools;

import com.spring.ai.demo.demo.Entities.Ticket;
import com.spring.ai.demo.demo.Services.Impl.TicketServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketDatabaseTool {
    private final TicketServiceImpl ticketService;

    @Tool(description = "Create a new ticket in the database")
    public Ticket CreateTicket(Ticket ticket) {
        return ticketService.createTicket(ticket);
    }
}
