# 🎬 AI Video Studio

**An AI-powered video generation platform built with Java, Spring Boot, React, and ComfyUI.**

AI Video Studio is a project for generating videos from text prompts and images using open-weight AI video-generation models. The backend communicates with ComfyUI to execute AI workflows, while Spring Boot exposes REST APIs for the frontend.

The long-term goal is to support multiple AI video models through a single platform, allowing users to generate, manage, and download AI-generated videos.

---

## 📌 Table of Contents

1. Project Overview
2. Features
3. Technology Stack
4. System Architecture
5. Understanding AI Video Generation
6. What is Wan 2.2?
7. Understanding ComfyUI
8. Project Structure
9. Prerequisites
10. Installation and Setup
11. Downloading and Configuring Wan 2.2
12. Running the Application
13. REST API Documentation
14. Understanding the Video Generation Flow
15. Configuration
16. Memory and Performance Considerations
17. Future Enhancements
18. Troubleshooting

---

## 1. Project Overview

AI Video Studio connects a web application with an AI video-generation engine.

A user can upload an image, provide a text prompt, and request video generation.

The Spring Boot backend receives the request, prepares the ComfyUI workflow, submits it for execution, and returns the generation response.

### Main objectives

- Generate videos from images.
- Generate videos using text prompts.
- Integrate different AI video-generation models.
- Manage video-generation requests.
- Store generated video metadata.
- Provide a simple interface for AI video creation.

**Current development status:** Backend integration with ComfyUI and Wan 2.2 image-to-video generation is implemented. Text-to-video currently needs a dedicated workflow.

---

## 2. Features

### Implemented

- Spring Boot REST API.
- ComfyUI integration using Spring RestClient.
- Image upload to ComfyUI.
- Image-to-video workflow submission.
- Positive and negative prompt configuration.
- Video generation using Wan 2.2.
- Configurable video dimensions and frame count.
- ComfyUI system statistics endpoint.

### Planned

- React frontend.
- Dedicated text-to-video workflow.
- Video-generation job status tracking.
- Video history.
- Video download API.
- Video deletion.
- Multiple AI model support.
- Cloud-based video-generation providers.
- User authentication and management.

---

## 3. Technology Stack

| Technology | Purpose |
|---|---|
| Java | Backend programming language |
| Spring Boot | REST API and application framework |
| Maven | Dependency management and build |
| React | Frontend user interface (planned) |
| ComfyUI | AI workflow execution engine |
| Wan 2.2 | AI video-generation model |
| Python | ComfyUI runtime |
| PyTorch | Deep learning framework |
| MySQL | Metadata storage (planned) |
| Git and GitHub | Version control |

---

## 4. System Architecture

The application follows a client-server architecture.

```text
              React Frontend
                    |
                    | HTTP REST API
                    |
                    v
             Spring Boot Backend
                    |
             VideoController
                    |
                    v
          VideoGenerationService
                    |
                    v
              ComfyUiClient
                    |
                    | HTTP API
                    |
                    v
                 ComfyUI
                    |
                    v
              Wan 2.2 Model
                    |
                    v
              Video Generation
                    |
                    v
                MP4 Output
```

### Component responsibilities

**React Frontend**

- Accepts user prompts.
- Uploads images.
- Displays generation results.
- Allows users to download generated videos.

**Spring Boot Backend**

- Exposes REST endpoints.
- Validates incoming requests.
- Prepares AI workflow JSON.
- Communicates with ComfyUI.
- Handles image uploads.

**ComfyUI**

- Loads the AI model.
- Executes the workflow.
- Connects model components.
- Generates video frames.
- Saves the final video.

**Wan 2.2**

- Processes the input image and text prompt.
- Generates new video frames.
- Creates motion based on the requested scene.

---

## 5. Understanding AI Video Generation

AI video generation uses deep learning models to create video frames from text, images, or other inputs.

There are two main generation approaches.

### A. Text-to-Video (T2V)

Text-to-video generates a video directly from a text description.

Example prompt:

"A woman walking through a beautiful garden during golden hour, cinematic lighting, realistic movement."

Input:

```text
Text Prompt
```

Output:

```text
AI-generated video
```

### B. Image-to-Video (I2V)

Image-to-video takes an existing image and generates motion from it.

Example:

- Upload a portrait photograph.
- Provide a prompt describing the desired movement.
- The model generates video frames based on the image and prompt.

Input:

```text
Image + Text Prompt
```

Output:

```text
Animated Video
```

Image-to-video is particularly useful for animating portraits, landscapes, characters, and product photographs.

**Important:** Image-to-video models do not guarantee perfect identity preservation. Facial features can change between generated frames.

---

## 6. What is Wan 2.2?

Wan 2.2 is an open-weight AI video-generation model family developed by Alibaba's Wan team.

It uses advanced generative deep learning techniques to create videos from text and images.

### What can Wan 2.2 do?

- Text-to-video generation.
- Image-to-video generation.
- Generate realistic scenes.
- Create cinematic motion.
- Animate uploaded photographs.

### Model used in this project

This project initially uses the Wan 2.2 TI2V 5B model.

Model file:

```text
wan2.2_ti2v_5B_fp16.safetensors
```

The 5B indicates approximately five billion model parameters.

### What are the other model files?

**1. Diffusion Model**

```text
wan2.2_ti2v_5B_fp16.safetensors
```

This is the main generative model. It performs the core video-generation computation.

**2. Text Encoder**

```text
umt5_xxl_fp8_e4m3fn_scaled.safetensors
```

The text encoder converts the user's natural-language prompt into numerical representations that the video model can understand.

**3. VAE**

```text
wan2.2_vae.safetensors
```

VAE stands for Variational Autoencoder.

It converts between the model's compressed latent representation and video/image pixels.

In simplified terms:

- Encoder: converts visual information into a compressed representation.
- Decoder: converts the generated representation back into visual frames.

### Simplified generation pipeline

```text
User Prompt
     |
     v
Text Encoder
     |
     v
Text Embeddings
     |
     v
Wan 2.2 Diffusion Model
     |
     v
Latent Video Representation
     |
     v
VAE Decoder
     |
     v
Video Frames
     |
     v
MP4 Output
```

### Official resources

- Wan GitHub: https://github.com/Wan-Video/Wan2.2
- ComfyUI: https://github.com/comfyanonymous/ComfyUI
- Hugging Face: https://huggingface.co/Wan-AI

---

## 7. Understanding ComfyUI

ComfyUI is a node-based interface and execution engine for generative AI workflows.

Instead of implementing the entire AI pipeline manually, we configure nodes and connect them to define how generation should happen.

### What is a workflow?

A workflow is a collection of connected nodes.

Each node performs a particular operation.

For example:

| Node | Responsibility |
|---|---|
| LoadImage | Loads the input image |
| CLIPLoader | Loads the text encoder |
| CLIPTextEncode | Converts prompts into conditioning |
| UNETLoader | Loads the diffusion model |
| VAELoader | Loads the VAE |
| KSampler | Performs the sampling process |
| VAEDecode | Converts latent output into visual frames |
| CreateVideo | Combines frames into a video |
| SaveVideo | Saves the generated MP4 |

### Workflow JSON

This project stores its workflow here:

```text
src/main/resources/comfyui/wan2-2-i2v-api.json
```

The backend loads this JSON and dynamically updates values such as:

- Positive prompt.
- Negative prompt.
- Uploaded image.
- Video width.
- Video height.
- Number of frames.

The workflow is then submitted to ComfyUI.

**Why use workflow JSON?**

It separates AI processing logic from Java business logic.

We can change the workflow without rewriting the entire Spring Boot application.

---

## 8. Project Structure

```text
video-service/
│
├── pom.xml
├── .gitignore
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── markai/
        │           └── videoservice/
        │               │
        │               ├── VideoServiceApplication.java
        │               │
        │               ├── client/
        │               │   └── ComfyUiClient.java
        │               │
        │               ├── config/
        │               │   ├── AppConfig.java
        │               │   ├── ComfyUiProperties.java
        │               │   └── MultipartConfig.java
        │               │
        │               ├── controller/
        │               │   ├── ComfyUiController.java
        │               │   ├── HealthController.java
        │               │   └── VideoController.java
        │               │
        │               ├── dto/
        │               │   └── VideoGenerationRequest.java
        │               │
        │               └── service/
        │                   └── VideoGenerationService.java
        │
        └── resources/
            ├── application.yml
            └── comfyui/
                └── wan2-2-i2v-api.json
```

### Understanding each Java package

**client/**

Contains the external service communication layer.

`ComfyUiClient.java`

- Connects to ComfyUI.
- Uploads images.
- Submits workflow JSON.
- Retrieves system statistics.

**config/**

Contains application configuration.

`AppConfig.java`

Defines application-level beans.

`ComfyUiProperties.java`

Holds ComfyUI connection configuration.

`MultipartConfig.java`

Configures the maximum image upload size.

**controller/**

Exposes HTTP endpoints.

`VideoController.java`

Receives video-generation requests.

`HealthController.java`

Provides health-check functionality.

`ComfyUiController.java`

Provides ComfyUI-related endpoints.

**dto/**

Contains request and response data transfer objects.

`VideoGenerationRequest.java`

Represents the video-generation request.

**service/**

Contains the main business logic.

`VideoGenerationService.java`

- Loads the workflow JSON.
- Updates prompt values.
- Updates uploaded image details.
- Configures frame dimensions and length.
- Submits the workflow to ComfyUI.

---

## 9. Prerequisites

Install the following software:

| Software | Purpose |
|---|---|
| JDK 24 | Java runtime and compilation |
| Maven | Build management |
| Python 3.11 | ComfyUI runtime |
| Git | Version control |
| ComfyUI | AI workflow engine |
| PyTorch | Deep learning runtime |

The development environment used for this project:

- MacBook Air M4
- 24 GB unified memory
- macOS
- Java 24
- Spring Boot 4.x
- Python 3.11
- ComfyUI

---

## 10. Installation and Setup

### Step 1: Clone the repository

```bash
git clone https://github.com/temptation4/ai-video-studio.git

cd ai-video-studio
```

### Step 2: Build the Spring Boot application

```bash
mvn clean install
```

### Step 3: Install ComfyUI

ComfyUI must be installed separately.

Repository:

https://github.com/comfyanonymous/ComfyUI

Example local setup:

```bash
mkdir -p ~/AI/comfyui

cd ~/AI/comfyui

git clone https://github.com/comfyanonymous/ComfyUI.git

cd ComfyUI

python3.11 -m venv ~/AI/comfyui/venv

source ~/AI/comfyui/venv/bin/activate

pip install -r requirements.txt
```

Install a PyTorch build compatible with your machine and ComfyUI requirements.

### Step 4: Download AI model files

Download the compatible Wan 2.2 model files from the official model repository.

Place them in these folders:

```text
ComfyUI/models/
│
├── diffusion_models/
│   └── wan2.2_ti2v_5B_fp16.safetensors
│
├── text_encoders/
│   └── umt5_xxl_fp8_e4m3fn_scaled.safetensors
│
└── vae/
    └── wan2.2_vae.safetensors
```

Make sure the model filenames match the names configured in the workflow JSON.

### Step 5: Start ComfyUI

```bash
cd ~/AI/comfyui/ComfyUI

source ~/AI/comfyui/venv/bin/activate

python main.py
```

ComfyUI normally runs at:

```text
http://127.0.0.1:8188
```

Open the browser:

http://127.0.0.1:8188

### Step 6: Configure Spring Boot

Update `application.yml` with the local ComfyUI address and other required application settings.

Example ComfyUI configuration:

```yaml
comfyui:
  base-url: http://127.0.0.1:8188
```

Use the configuration property names defined by the application.

### Step 7: Start Spring Boot

```bash
mvn spring-boot:run
```

The backend runs on:

```text
http://localhost:8098
```

---

## 11. Running the Application

Start services in this order:

**First: Start ComfyUI**

```bash
python main.py
```

**Second: Start Spring Boot**

```bash
mvn spring-boot:run
```

**Third: Verify the backend**

```bash
curl http://localhost:8098/api/v1/health
```

**Fourth: Check ComfyUI statistics**

```bash
curl http://localhost:8098/api/v1/comfyui/system-stats
```

---

## 12. REST API Documentation

Base URL:

```text
http://localhost:8098/api/v1
```

### 1. Health Check

```http
GET /health
```

Checks whether the backend application is running.

### 2. ComfyUI System Statistics

```http
GET /comfyui/system-stats
```

Retrieves system information from ComfyUI.

### 3. Generate Video

```http
POST /videos/generate
Content-Type: application/json
```

Example request:

```json
{
  "prompt": "A beautiful cinematic sunset over the ocean",
  "negativePrompt": "blurry, distorted, low quality"
}
```

This endpoint currently submits the configured workflow. The existing workflow is image-to-video, so a dedicated text-to-video workflow is still required for genuine text-to-video generation.

### 4. Image-to-Video

```http
POST /videos/image-to-video
Content-Type: multipart/form-data
```

Postman form-data:

| Key | Type | Value |
|---|---|---|
| image | File | Upload image |
| prompt | Text | Video description |
| negativePrompt | Text | Unwanted visual characteristics |

Example positive prompt:

```text
A cinematic video of a woman walking
gracefully through a beautiful garden.
Natural movement, golden sunlight,
realistic appearance and smooth animation.
```

Example negative prompt:

```text
distorted face, extra fingers, flickering,
jittering, blurry frames, identity change,
unnatural movement, low quality.
```

The API submits the workflow to ComfyUI and returns the queue response.

A successful queue response means the workflow was accepted. It does not necessarily mean the video has finished generating.

---

## 13. Understanding the Video Generation Flow

Let's understand what happens internally when a user uploads an image.

**Step 1: User uploads an image**

The frontend sends the image and prompt to Spring Boot.

**Step 2: VideoController receives the request**

The controller accepts the multipart request.

**Step 3: VideoGenerationService processes the request**

The service uploads the image to ComfyUI and receives the uploaded image name.

**Step 4: Load the workflow JSON**

Spring Boot reads:

```text
wan2-2-i2v-api.json
```

**Step 5: Update workflow nodes**

The service dynamically updates:

- Node 6: Positive prompt.
- Node 7: Negative prompt.
- Node 56: Uploaded image.
- Node 55: Video width, height and frame length.

**Step 6: Submit the workflow**

Spring Boot sends the updated workflow to:

```http
POST http://127.0.0.1:8188/prompt
```

**Step 7: ComfyUI executes the workflow**

ComfyUI loads the required model components and generates the video.

**Step 8: Save the generated video**

The workflow saves the MP4 output to the ComfyUI output directory.

Example:

```text
~/AI/comfyui/ComfyUI/output/video/
```

---

## 14. Configuration and Generation Parameters

The video-generation workflow contains important parameters.

| Parameter | Meaning |
|---|---|
| Width | Output video width in pixels |
| Height | Output video height in pixels |
| Length | Number of generated frames |
| FPS | Frames displayed per second |
| Prompt | Description of desired video |
| Negative Prompt | Characteristics to avoid |

### Understanding frame count and duration

Formula:

```text
Duration = Number of Frames / FPS
```

Example:

```text
Frames = 49
FPS = 24

Duration = 49 / 24

Duration ≈ 2.04 seconds
```

Increasing frame count generates a longer video and generally requires more processing time and memory.

Increasing resolution also increases computational requirements.

---

## 15. Memory and Performance Considerations

AI video generation is resource-intensive.

The development machine has 24 GB unified memory.

Apple Silicon uses unified memory shared by the CPU, GPU, operating system and applications.

### Recommendations

- Start with smaller resolutions.
- Generate short videos while testing.
- Avoid running multiple large generation jobs simultaneously.
- Close unnecessary applications.
- Monitor memory and swap usage.
- Allow ComfyUI to unload models when switching workflows if needed.

Model files stored on disk do not consume the same amount of RAM as models loaded for inference.

Keeping multiple models installed does not mean all of them must be loaded simultaneously.

---

## 16. Future Enhancements

### Phase 1: Backend Foundation

- [x] Spring Boot application
- [x] ComfyUI integration
- [x] Image upload
- [x] Wan 2.2 image-to-video workflow
- [x] Prompt configuration

### Phase 2: Video Management

- [ ] Generation job tracking
- [ ] Video history
- [ ] Download generated videos
- [ ] Delete generated videos
- [ ] Persist video metadata in MySQL

### Phase 3: Frontend

- [ ] React application
- [ ] Image upload UI
- [ ] Prompt editor
- [ ] Video preview
- [ ] Generation progress
- [ ] Video history page

### Phase 4: Multiple AI Models

- [ ] Dedicated text-to-video workflow
- [ ] LTX-Video integration
- [ ] Multiple model selection
- [ ] Hosted video-generation API integration
- [ ] Common model-provider abstraction

### Phase 5: Production Readiness

- [ ] Authentication and authorization
- [ ] Asynchronous job processing
- [ ] Queue management
- [ ] Error handling and retries
- [ ] Monitoring and logging
- [ ] Cloud deployment

---

## 17. Troubleshooting

### Problem 1: ComfyUI connection refused

Check whether ComfyUI is running:

```bash
curl http://127.0.0.1:8188/system_stats
```

### Problem 2: Model not found

Check that the model files are located in the correct ComfyUI directories and that their filenames match the workflow configuration.

### Problem 3: Upload size exceeded

Check the multipart configuration in Spring Boot.

### Problem 4: Video generation takes too long

Reduce resolution and frame count during testing. Check memory usage and ensure that only one heavy generation job is running.

### Problem 5: Generated video is blurry or distorted

Experiment with prompt quality, resolution, model settings and workflow configuration. Increasing resolution alone does not guarantee better identity consistency or motion quality.

---

## 18. Contributing

This is an actively developing project.

Suggestions, improvements and contributions are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Implement your changes.
4. Test your changes.
5. Submit a pull request.

---

## Author and Repository

**Project:** AI Video Studio

**GitHub:** https://github.com/temptation4/ai-video-studio

**Primary backend:** Java / Spring Boot

**AI engine:** ComfyUI

**Initial video model:** Wan 2.2 TI2V 5B

---

*AI Video Studio — Building an intelligent platform for AI-powered video generation.*
