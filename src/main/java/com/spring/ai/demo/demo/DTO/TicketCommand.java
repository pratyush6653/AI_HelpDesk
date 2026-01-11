package com.spring.ai.demo.demo.DTO;

import com.spring.ai.demo.demo.Enums.Priority;
import com.spring.ai.demo.demo.Enums.Status;

public record TicketCommand(String userName,
                            String email,
                            String summary,
                            Priority priority,
                            Status status
) {
}
