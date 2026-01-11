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
                    .tools(ticketDatabaseTool)
                    .system(systemPromptResource)
                    .options(ChatOptions.builder()
                            .model(modelName)
                            .build())
                    .user(request.messages().content())
                    .call()
                    .entity(OpenRouterResponse.class);
            return content;

        } catch (Exception ex) {
            log.error("Model call failed: {}", ex.getLocalizedMessage());
            throw new RuntimeException("Model call failed: " + modelName);
        }
    }


}

