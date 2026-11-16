
package com.markai.videoservice.client;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ComfyUiClient {

    private final RestClient restClient;

    public ComfyUiClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://127.0.0.1:8188")
                .build();
    }

    public String getSystemStats() {
        return restClient.get()
                .uri("/system_stats")
                .retrieve()
                .body(String.class);
    }

    // Upload image to ComfyUI
    public String uploadImage(MultipartFile file) throws IOException {

        ByteArrayResource imageResource =
                new ByteArrayResource(file.getBytes()) {

                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename() != null
                                ? file.getOriginalFilename()
                                : "uploaded-image.png";
                    }
                };

        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        body.add("image", imageResource);
        body.add("type", "input");
        body.add("overwrite", "true");

        return restClient.post()
                .uri("/upload/image")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(String.class);
    }

    // Submit workflow to ComfyUI
    public String queuePrompt(String workflowJson) {

        String requestBody = """
                {
                  "prompt": %s
                }
                """.formatted(workflowJson);

        return restClient.post()
                .uri("/prompt")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }
}
