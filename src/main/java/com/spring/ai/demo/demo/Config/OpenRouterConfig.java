package com.spring.ai.demo.demo.Config;

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

}
