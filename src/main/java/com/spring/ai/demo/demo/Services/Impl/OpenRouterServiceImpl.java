package com.spring.ai.demo.demo.Services.Impl;

import com.spring.ai.demo.demo.DTO.OpenRouterRequest;
import com.spring.ai.demo.demo.DTO.OpenRouterResponse;
import com.spring.ai.demo.demo.Services.OpenRouterServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class OpenRouterServiceImpl implements OpenRouterServices {

    private final ChatClient chatClient;
    private final String fallbackModel;
    private final String primaryModel;

    public OpenRouterServiceImpl(ChatClient.Builder builder,
                                 @Value("${spring.ai.openai.chat.options.model}") String primaryModel,
                                 @Value("${spring.ai.openai.chat.fallback-model}") String fallbackModel) {
        this.chatClient = builder.build();
        this.primaryModel = primaryModel;
        this.fallbackModel = fallbackModel;
    }

    public List<OpenRouterResponse> processMessage(OpenRouterRequest request) {
        try {
            // Call primary model
            return callModel(chatClient, request, primaryModel);
        } catch (Exception e) {
            log.error("⚠️ Primary model failed. Switching to fallback model...");
            // Call fallback model using the same ChatClient but override model per request
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
                    .entity(new ParameterizedTypeReference<List<OpenRouterResponse>>() {
                        @Override
                        public Type getType() {
                            return super.getType();
                        }
                    });
            return content.stream().map(r -> new OpenRouterResponse(
                    r.role(),
                    r.title(),
                    r.content(),
                    modelName,
                    Instant.now()
            )).toList();

        } catch (Exception ex) {
            log.error("Model call failed: {}", ex.getLocalizedMessage());
            throw new RuntimeException("Model call failed: " + modelName);
        }
    }


}

