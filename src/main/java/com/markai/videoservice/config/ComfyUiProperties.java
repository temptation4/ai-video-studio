package com.markai.videoservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "comfyui")
public record ComfyUiProperties(String baseUrl) {
}
