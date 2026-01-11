package com.spring.ai.demo.demo.Services;

import com.spring.ai.demo.demo.DTO.OpenRouterRequest;
import com.spring.ai.demo.demo.DTO.OpenRouterResponse;

public interface OpenRouterServices {
    public OpenRouterResponse processMessage(OpenRouterRequest request);
}
