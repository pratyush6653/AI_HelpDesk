package com.spring.ai.demo.demo.Controllers;

import com.spring.ai.demo.demo.DTO.OpenRouterRequest;
import com.spring.ai.demo.demo.DTO.OpenRouterResponse;
import com.spring.ai.demo.demo.Services.OpenRouterServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final OpenRouterServices chatService;
    private final ChatMemory chatMemory;

    @PostMapping("/message")
    public ResponseEntity<OpenRouterResponse> sendMessage(
            @RequestHeader(value = "X-Conversation-Id", required = false) String conversationId,
            @RequestBody OpenRouterRequest chatRequest) {
        log.info("🔄 Received chat request from client");
        if (conversationId == null || conversationId.isBlank()) {
            conversationId = UUID.randomUUID().toString();
            log.info("🆕 New conversation created: {}", conversationId);
        }
        chatMemory.add(conversationId, new UserMessage(chatRequest.messages().content()));
        OpenRouterResponse openRouterResponse = chatService.processMessage(chatRequest, conversationId);
        log.info("📬 Sending response back to client");
        return ResponseEntity.ok()
                .header("X-Conversation-Id", conversationId)
                .body(openRouterResponse);
    }

//    @PostMapping("/stream")
//    public Flux<String> streamResponse(
//            @RequestHeader(value = "X-Conversation-Id", required = false) String conversationId,
//            @RequestBody OpenRouterRequest chatRequest) {
//        log.info("🔄 Received chat request from client");
//        if (conversationId == null || conversationId.isBlank()) {
//            conversationId = UUID.randomUUID().toString();
//            log.info("🆕 New conversation created: {}", conversationId);
//        }
//        Flux<String> responseFlux = chatService.streamMessage(chatRequest, conversationId);
//        log.info("📬 Streaming response back to client");
//        return responseFlux;
//    }

}
