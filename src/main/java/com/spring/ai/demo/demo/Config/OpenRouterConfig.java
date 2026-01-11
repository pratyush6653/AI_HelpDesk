package com.spring.ai.demo.demo.Config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenRouterConfig {
    @Value("${spring.ai.openai.chat.options.model}")
    private String primaryModel;

    @Value("${spring.ai.openai.chat.fallback-model}")
    private String fallbackModel;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        // You can customize default options here
        return builder
                .defaultSystem("You are doctor represtative and your name is liza   Always reply in a concise and normal human  manner.")
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
}
