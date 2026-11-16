package com.markai.videoservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ComfyUiProperties.class)
public class AppConfig {
}
