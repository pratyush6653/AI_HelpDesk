package com.spring.ai.demo.demo.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@Configuration
public class OpenRouterConfig {
    @Value("${spring.ai.openai.chat.options.model}")
    private String primaryModel;

    @Value("${spring.ai.openai.chat.fallback-model}")
    private String fallbackModel;

    @Value("classpath:/Helpdesk-System.st")
    private Resource systemPromptResource;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        log.info("ChatClient has been created");
        log.info("Chat memory instance: {}", chatMemory.getClass().getName());
        // You can customize default options here
        return builder
                .defaultSystem(systemPromptResource)
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

    @Bean
    public ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository repository) {
        return MessageWindowChatMemory.builder().chatMemoryRepository(repository)
                .maxMessages(20)
                .build();
    }

}
