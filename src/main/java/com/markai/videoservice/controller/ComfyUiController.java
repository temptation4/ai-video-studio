
package com.markai.videoservice.controller;

import com.markai.videoservice.client.ComfyUiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comfyui")
public class ComfyUiController {

    private final ComfyUiClient comfyUiClient;

    public ComfyUiController(ComfyUiClient comfyUiClient) {
        this.comfyUiClient = comfyUiClient;
    }

    @GetMapping("/system-stats")
    public String getSystemStats() {
        return comfyUiClient.getSystemStats();
    }
}
