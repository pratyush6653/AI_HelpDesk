package com.spring.ai.demo.demo.Controllers;

import com.spring.ai.demo.demo.DTO.OpenRouterRequest;
import com.spring.ai.demo.demo.DTO.OpenRouterResponse;
import com.spring.ai.demo.demo.Services.OpenRouterServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final OpenRouterServices chatService;

    @PostMapping("/message")
    public ResponseEntity<List<OpenRouterResponse>> sendMessage(@RequestBody OpenRouterRequest chatRequest) {
        log.info("🔄 Received chat request from client");
        List<OpenRouterResponse> openRouterResponse = chatService.processMessage(chatRequest);
        log.info("📬 Sending response back to client");
        return ResponseEntity.ok(openRouterResponse);
    }

}
