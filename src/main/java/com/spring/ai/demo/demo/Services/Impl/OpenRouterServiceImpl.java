package com.spring.ai.demo.demo.Services.Impl;


import com.spring.ai.demo.demo.DTO.OpenRouterRequest;
import com.spring.ai.demo.demo.DTO.OpenRouterResponse;
import com.spring.ai.demo.demo.Services.OpenRouterServices;
import com.spring.ai.demo.demo.Tools.TicketDatabaseTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
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

    public OpenRouterResponse processMessage(OpenRouterRequest request) {
        try {
            return callModel(chatClient, request, primaryModel);
        } catch (Exception e) {
            log.error("⚠️ Primary model failed. Switching to fallback model...");
            return callModel(chatClient, request, fallbackModel);
        }
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000)
    )
    private OpenRouterResponse callModel(ChatClient client,
                                         OpenRouterRequest request,
                                         String modelName) {
        try {
            OpenRouterResponse content = client.prompt()
                    .system(systemPromptResource)
                    .system("It is mandatory to use the provided tools to assist with help desk ticket management.")
                    .tools(ticketDatabaseTool)
                    .options(ChatOptions.builder()
                            .model(modelName)
                            .build())
                    .user(request.messages().content())
                    .call()
                    .entity(OpenRouterResponse.class);
            Instant parsedAt = parseTimestamp(content.timestamp());
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

        // Already valid ISO-8601
        if (ts.endsWith("Z") || ts.contains("+")) {
            return Instant.parse(ts);
        }

        // LLM forgot timezone → force UTC
        return Instant.parse(ts + "Z");
    }

}

