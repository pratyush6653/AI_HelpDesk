package com.spring.ai.demo.demo.DTO;

import java.time.Instant;

public record OpenRouterResponse(String role,
                                 String title,
                                 String content,
                                 Instant timestamp
) {
}
