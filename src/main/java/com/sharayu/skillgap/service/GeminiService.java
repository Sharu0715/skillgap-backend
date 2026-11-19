package com.sharayu.skillgap.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private static final Logger log =
            LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public String getRecommendation(String prompt) {

        log.info("Sending request to Gemini API");

        String url = apiUrl + "?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents",
                List.of(
                        Map.of(
                                "parts",
                                List.of(
                                        Map.of(
                                                "text",
                                                prompt
                                        )
                                )
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        try {

            long startTime = System.currentTimeMillis();

            log.info("Gemini API request started");

            ResponseEntity<Map> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            Map.class
                    );

            long endTime = System.currentTimeMillis();

            log.info("Gemini API response time: {} ms",
                    endTime - startTime);

            Map candidate =
                    (Map) ((List<?>) response.getBody()
                            .get("candidates"))
                            .get(0);

            Map content =
                    (Map) candidate.get("content");

            Map part =
                    (Map) ((List<?>) content.get("parts"))
                            .get(0);

            log.info("Gemini response received successfully.");

            return part.get("text").toString();

        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                log.error("Gemini API request timed out.", e);
                return "AI recommendation is taking too long. Please try again.";
            }

            log.error("Unable to connect to Gemini API.", e);
            return "AI recommendation service is temporarily unavailable.";

        } catch (Exception e) {
            log.error("Unexpected error while processing Gemini response.", e);
            return "Unable to generate AI recommendation at this time.";
        }
    }
}