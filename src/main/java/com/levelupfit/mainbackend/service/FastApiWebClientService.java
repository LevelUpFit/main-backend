package com.levelupfit.mainbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levelupfit.mainbackend.dto.feedback.request.ExerciseFeedbackRequest;
import com.levelupfit.mainbackend.dto.feedback.response.FeedbackresultDTO;
import org.springframework.core.io.ByteArrayResource;
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

    public Mono<FeedbackresultDTO> sendToFastApi(ExerciseFeedbackRequest request) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        MultipartFile video = request.getVideo();
        byte[] bytes = video.getBytes();

        ByteArrayResource videoResource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return video.getOriginalFilename(); // 안 하면 null 처리됨
            }
        };
        builder.part("file", videoResource)
                .header("Content-Disposition", "form-data; name=file; filename=" + video.getOriginalFilename());
        builder.part("exercise_id", request.getExerciseId());
        builder.part("feedback_id", request.getFeedbackId());
        builder.part("level", request.getLevel());


        return webClient.post()
                .uri("") //코드 env설정 후 수정
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(FeedbackresultDTO.class);
    }

}
