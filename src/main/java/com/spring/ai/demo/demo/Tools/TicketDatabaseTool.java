package com.spring.ai.demo.demo.Tools;

import com.spring.ai.demo.demo.DTO.TicketCommand;
import com.spring.ai.demo.demo.DTO.TicketView;
import com.spring.ai.demo.demo.Entities.Ticket;
import com.spring.ai.demo.demo.Services.Impl.TicketServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketDatabaseTool {
    private final TicketServiceImpl ticketService;

    @Tool(description = "Create a new ticket in the database")
    public TicketView CreateTicket(TicketCommand input) {
        Ticket createdTicket = ticketService.createTicket(input);
        return toView(createdTicket);
    }

    @Tool(description = "ftech ticket details  by user name from the database")
    public TicketView GetTicketByUserName(String userName) {
        Ticket t = ticketService.getTicketByUserName(userName);
        return toView(t);
    }

    @Tool(description = "Update an existing ticket in the database")
    public TicketView updateTicket(@ToolParam(description = "Ticket ID") Long ticketId, TicketCommand ticket) {
        Ticket updatedTicket = ticketService.updateTicket(ticket, ticketId);
        return toView(updatedTicket);
    }

    private TicketView toView(Ticket input) {
        return new TicketView(
                input.getId(),
                input.getUserName(),
                input.getEmail(),
                input.getSummary(),
                input.getStatus(),
                input.getPriority(),
                input.getCreatedAt()
        );

    }
}
