package com.spring.ai.demo.demo.DTO;

public record ChatMessage(String role,     // user | assistant | system
                          String content) {
}
