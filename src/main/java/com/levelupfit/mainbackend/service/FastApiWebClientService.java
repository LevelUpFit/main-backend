package com.levelupfit.mainbackend.service;

import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class FastApiWebClientService {

    private final WebClient webClient;

    public FastApiWebClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> sendToFastApi(MultipartFile video, int exerciseId, int feedbackId) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("video", video.getResource());
        builder.part("exercise_id", exerciseId);
        builder.part("feedback_id", feedbackId);

        return webClient.post()
                .uri("http://localhost:8000/analyze") //코드 env설정 후 수정
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(String.class);
    }

}
