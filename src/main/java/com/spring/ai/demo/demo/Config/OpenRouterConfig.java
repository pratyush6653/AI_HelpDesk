package com.spring.ai.demo.demo.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class OpenRouterConfig {
    @Value("${spring.ai.openai.chat.options.model}")
    private String primaryModel;

    @Value("${spring.ai.openai.chat.fallback-model}")
    private String fallbackModel;

    @Value("classpath:/Helpdesk-System.st")
    private Resource systemPromptResource;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        // You can customize default options here
        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public String primaryModel() {
        return primaryModel;
    }

    @Bean
    public String fallbackModel() {
        return fallbackModel;
    }

    @Bean
    public Resource systemPromptResource() {
        return systemPromptResource;
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register JavaTimeModule to handle java.time.* classes
        mapper.registerModule(new JavaTimeModule());
        // Write dates as ISO-8601 strings
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

}
