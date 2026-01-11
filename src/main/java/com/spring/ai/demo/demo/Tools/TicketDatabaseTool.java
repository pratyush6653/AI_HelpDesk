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

    @Tool(description = "Create a new help desk ticket in the database. Use ONLY when all required fields are available.")
    public TicketView createTicket(
            @ToolParam(description = "Ticket details including userName, email, summary, priority")
            TicketCommand input
    ) {
        Ticket createdTicket = ticketService.createTicket(input);
        return toView(createdTicket);
    }

    @Tool(description = "Fetch ticket details and current status by ticket ID. MUST be used when ticketId is provided by the user.")
    public TicketView getTicketById(
            @ToolParam(description = "Unique ticket ID provided by the user")
            Long ticketId
    ) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        return toView(ticket);
    }

    public TicketView getTicketByUserName(
            @ToolParam(description = "User name associated with the ticket")
            String userName
    ) {
        Ticket ticket = ticketService.getTicketByUserName(userName);
        return toView(ticket);
    }

    @Tool(description = "Update an existing ticket using ticket ID. Use when user wants to modify summary, priority, or status.")
    public TicketView updateTicket(
            @ToolParam(description = "Unique ticket ID of the ticket to update")
            Long ticketId,

            @ToolParam(description = "Updated ticket fields")
            TicketCommand ticket
    ) {
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
