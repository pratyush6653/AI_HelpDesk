package com.spring.ai.demo.demo.DTO;

public record OpenRouterResponse(String role,
                                 String title,
                                 String content,
                                 String timestamp,
                                 boolean toolUsed
) {
}
