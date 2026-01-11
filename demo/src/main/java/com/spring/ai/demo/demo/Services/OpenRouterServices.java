package com.spring.ai.demo.demo.Services;

import com.spring.ai.demo.demo.DTO.OpenRouterRequest;
import com.spring.ai.demo.demo.DTO.OpenRouterResponse;

import java.util.List;

public interface OpenRouterServices {
    public List<OpenRouterResponse> processMessage(OpenRouterRequest request);
}
