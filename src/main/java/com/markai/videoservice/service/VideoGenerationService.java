
package com.markai.videoservice.service;

import com.markai.videoservice.client.ComfyUiClient;
import com.markai.videoservice.dto.VideoGenerationRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;

@Service
public class VideoGenerationService {

    private static final String WORKFLOW_PATH =
            "comfyui/wan2-2-i2v-api.json";

    private static final int VIDEO_LENGTH = 49;

    private final ComfyUiClient comfyUiClient;
    private final ObjectMapper objectMapper;

    public VideoGenerationService(
            ComfyUiClient comfyUiClient,
            ObjectMapper objectMapper
    ) {
        this.comfyUiClient = comfyUiClient;
        this.objectMapper = objectMapper;
    }

    // Text-to-Video
    public String generateVideo(VideoGenerationRequest request)
            throws IOException {

        JsonNode workflow = loadWorkflow();

        updatePrompt(
                workflow,
                request.prompt(),
                request.negativePrompt()
        );

        return submitWorkflow(workflow);
    }

    // Image-to-Video
    public String generateImageToVideo(
            MultipartFile image,
            String prompt,
            String negativePrompt
    ) throws IOException {

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please upload an image"
            );
        }

        // Upload image to ComfyUI
        String uploadResponse =
                comfyUiClient.uploadImage(image);

        JsonNode uploadResult =
                objectMapper.readTree(uploadResponse);

        String imageName =
                uploadResult.path("name").asString();

        if (imageName.isBlank()) {
            throw new IllegalStateException(
                    "ComfyUI did not return an image filename"
            );
        }

        String subfolder =
                uploadResult.path("subfolder").asString();

        if (!subfolder.isBlank()) {
            imageName = subfolder + "/" + imageName;
        }

        // Load workflow
        JsonNode workflow = loadWorkflow();

        // Update prompts
        updatePrompt(workflow, prompt, negativePrompt);

        // Update uploaded image (node 56)
        ObjectNode imageNode =
                getNodeInputs(workflow, "56");

        imageNode.put("image", imageName);

        // Increase video duration (node 55)
        ObjectNode videoNode =
                getNodeInputs(workflow, "55");

        videoNode.put("length", VIDEO_LENGTH);

        videoNode.put("width", 768);
        videoNode.put("height", 432);

        // Submit workflow
        return submitWorkflow(workflow);
    }

    // Load ComfyUI API workflow
    private JsonNode loadWorkflow() throws IOException {

        try (InputStream inputStream =
                     new ClassPathResource(WORKFLOW_PATH)
                             .getInputStream()) {

            return objectMapper.readTree(inputStream);
        }
    }

    // Update positive and negative prompts
    private void updatePrompt(
            JsonNode workflow,
            String prompt,
            String negativePrompt
    ) {

        ObjectNode positiveNode =
                getNodeInputs(workflow, "6");

        positiveNode.put("text", prompt);

        ObjectNode negativeNode =
                getNodeInputs(workflow, "7");

        negativeNode.put(
                "text",
                negativePrompt == null ? "" : negativePrompt
        );
    }

    // Get workflow node inputs safely
    private ObjectNode getNodeInputs(
            JsonNode workflow,
            String nodeId
    ) {

        JsonNode node = workflow.path(nodeId);

        if (!(node instanceof ObjectNode)) {
            throw new IllegalStateException(
                    "Workflow node not found: " + nodeId
            );
        }

        JsonNode inputs = node.path("inputs");

        if (!(inputs instanceof ObjectNode)) {
            throw new IllegalStateException(
                    "Inputs not found for workflow node: " + nodeId
            );
        }

        return (ObjectNode) inputs;
    }

    // Submit workflow to ComfyUI
    private String submitWorkflow(JsonNode workflow)
            throws IOException {

        String workflowJson =
                objectMapper.writeValueAsString(workflow);

        return comfyUiClient.queuePrompt(workflowJson);
    }

}
