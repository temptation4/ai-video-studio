
package com.markai.videoservice.dto;

public record VideoGenerationRequest(
        String prompt,
        String negativePrompt
) {
}
