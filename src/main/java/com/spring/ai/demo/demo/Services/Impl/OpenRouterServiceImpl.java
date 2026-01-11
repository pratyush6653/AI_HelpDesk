package com.spring.ai.demo.demo.Services.Impl;

import com.spring.ai.demo.demo.DTO.OpenRouterRequest;
import com.spring.ai.demo.demo.DTO.OpenRouterResponse;
import com.spring.ai.demo.demo.Services.OpenRouterServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenRouterServiceImpl implements OpenRouterServices {

    private final ChatClient chatClient;
    private final String fallbackModel;
    private final String primaryModel;

    public List<OpenRouterResponse> processMessage(OpenRouterRequest request) {
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
    private List<OpenRouterResponse> callModel(ChatClient client,
                                               OpenRouterRequest request,
                                               String modelName) {
        try {
            List<OpenRouterResponse> content = client.prompt()
                    .options(ChatOptions.builder()
                            .model(modelName)
                            .build())
                    .user(request.messages().content())
                    .call()
                    .entity(new org.springframework.core.ParameterizedTypeReference<List<OpenRouterResponse>>() {
                    });

            return content.stream().map(r -> new OpenRouterResponse(
                    r.role(),
                    r.title(),
                    r.content(),
                    Instant.now()
            )).toList();

        } catch (Exception ex) {
            log.error("Model call failed: {}", ex.getLocalizedMessage());
            throw new RuntimeException("Model call failed: " + modelName);
        }
    }


}

