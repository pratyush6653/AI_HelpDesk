package com.spring.ai.demo.demo.Services.Impl;


import com.spring.ai.demo.demo.DTO.OpenRouterRequest;
import com.spring.ai.demo.demo.DTO.OpenRouterResponse;
import com.spring.ai.demo.demo.Services.OpenRouterServices;
import com.spring.ai.demo.demo.Tools.TicketDatabaseTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenRouterServiceImpl implements OpenRouterServices {
    private final Resource systemPromptResource;
    private final ChatClient chatClient;
    private final String fallbackModel;
    private final String primaryModel;
    private final TicketDatabaseTool ticketDatabaseTool;
    private final ChatMemory chatMemory;

    public OpenRouterResponse processMessage(OpenRouterRequest request, String conversationId) {
        try {
            return callModel(chatClient, request, primaryModel, conversationId);
        } catch (Exception e) {
            log.error("⚠️ Primary model failed. Switching to fallback model...");
            return callModel(chatClient, request, fallbackModel, conversationId);
        }
    }

//    public Flux<String> streamMessage(OpenRouterRequest request, String conversationId) {
//        try {
//            return StreamResponse(chatClient, request, primaryModel, conversationId);
//        } catch (Exception e) {
//            log.error("⚠️ Primary model failed. Switching to fallback model...");
//            return StreamResponse(chatClient, request, fallbackModel, conversationId);
//        }
//    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000)
    )
    private OpenRouterResponse callModel(ChatClient client,
                                         OpenRouterRequest request,
                                         String modelName, String conversationId) {
        try {
            chatMemory.add(conversationId, new UserMessage(request.messages().content()));
            OpenRouterResponse content = client.prompt()
                    .system(systemPromptResource)
                    .system("It is mandatory to use the provided tools to assist with help desk ticket management.")
                    .system("use the tools exactly as described ")
                    .tools(ticketDatabaseTool)
                    .options(ChatOptions.builder()
                            .model(modelName)
                            .build())
                    .user(request.messages().content())
                    .call()
                    .entity(OpenRouterResponse.class);
            Instant parsedAt = parseTimestamp(content.timestamp());
            if (content.toolUsed()) {
                chatMemory.clear(conversationId);
                log.info("🧹 Cleared conversation memory for conversationId {}", conversationId);
            }
            return content;

        } catch (Exception ex) {
            log.error("Model call failed: {}", ex.getLocalizedMessage());
            throw new RuntimeException("Model call failed: " + modelName);
        }
    }

    private Instant parseTimestamp(String ts) {
        if (ts == null || ts.isBlank()) {
            return Instant.now();
        }
        if (ts.endsWith("Z") || ts.contains("+")) {
            return Instant.parse(ts);
        }

        // Model  forgot timezone → force UTC
        return Instant.parse(ts + "Z");
    }


//    public Flux<String> StreamResponse(
//            ChatClient client,
//            OpenRouterRequest request,
//            String modelName, String conversationId) {
//        try {
//            chatMemory.add(conversationId, new UserMessage(request.messages().content()));
//            AtomicBoolean toolUsed = new AtomicBoolean(false);
//            return client.prompt()
//                    .system(systemPromptResource)
//                    .system("It is mandatory to use the provided tools to assist with help desk ticket management.")
//                    .system("use the tools exactly as described ")
//                    .tools(ticketDatabaseTool)
//                    .options(ChatOptions.builder()
//                            .model(modelName)
//                            .build())
//                    .user(request.messages().content())
//                    .stream()
//                    .content()
//                    .doOnComplete(() -> {
//                        if (toolUsed.get()) {
//                            chatMemory.clear(conversationId);
//                            log.info("🧹 Cleared conversation memory for conversationId {}", conversationId);
//                        }
//                    });
//        } catch (Exception ex) {
//            log.error("Model call failed: {}", ex.getLocalizedMessage());
//            throw new RuntimeException("Model call failed: " + modelName);
//        }
//    }
}

