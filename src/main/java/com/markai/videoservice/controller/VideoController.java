
package com.markai.videoservice.controller;

import com.markai.videoservice.dto.VideoGenerationRequest;
import com.markai.videoservice.service.VideoGenerationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    private final VideoGenerationService videoGenerationService;

    public VideoController(VideoGenerationService videoGenerationService) {
        this.videoGenerationService = videoGenerationService;
    }


    @PostMapping("/generate")
    public ResponseEntity<String> generateVideo(
            @RequestBody VideoGenerationRequest request)
            throws Exception {

        String response =
                videoGenerationService.generateVideo(request);

        return ResponseEntity.ok(response);
    }


    @PostMapping(
            value = "/image-to-video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> generateImageToVideo(
            @RequestParam("image") MultipartFile image,
            @RequestParam("prompt") String prompt,
            @RequestParam(value = "negativePrompt", required = false)
            String negativePrompt
    ) throws IOException {

        String response = videoGenerationService.generateImageToVideo(
                image,
                prompt,
                negativePrompt
        );

        return ResponseEntity.ok(response);
    }

}
