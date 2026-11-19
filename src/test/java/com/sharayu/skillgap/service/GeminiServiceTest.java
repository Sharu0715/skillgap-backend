
package com.sharayu.skillgap.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)
class GeminiServiceTest {


    @Test
    void getRecommendation_shouldReturnText_whenApiResponseIsSuccessful() {

        java.util.Map<String, Object> part =
                java.util.Map.of("text", "Keep learning Java");

        java.util.Map<String, Object> content =
                java.util.Map.of("parts", java.util.List.of(part));

        java.util.Map<String, Object> candidate =
                java.util.Map.of("content", content);

        java.util.Map<String, Object> responseBody =
                java.util.Map.of("candidates", java.util.List.of(candidate));

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        String result = geminiService.getRecommendation("Suggest Java skills");

        assertEquals("Keep learning Java", result);
    }


    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeminiService geminiService;

    @Test
    void getRecommendation_shouldReturnFallbackMessage_whenApiFails() {

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenThrow(new RuntimeException("API error"));

        String result = geminiService.getRecommendation("Suggest Java skills");

        assertEquals(
                "Unable to generate AI recommendation at this time.",
                result
        );
    }

    @Test
    void getRecommendation_shouldReturnFallbackMessage_whenCandidatesKeyIsMissing() {

        java.util.Map<String, Object> responseBody =
                java.util.Map.of("message", "No candidates available");

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        String result = geminiService.getRecommendation("Suggest Java skills");

        assertEquals(
                "Unable to generate AI recommendation at this time.",
                result
        );
    }

    @Test
    void getRecommendation_shouldReturnFallbackMessage_whenContentKeyIsMissing() {

        java.util.Map<String, Object> candidate =
                java.util.Map.of("finishReason", "STOP");

        java.util.Map<String, Object> responseBody =
                java.util.Map.of("candidates", java.util.List.of(candidate));

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        String result = geminiService.getRecommendation("Suggest Java skills");

        assertEquals(
                "Unable to generate AI recommendation at this time.",
                result
        );
    }

    @Test
    void getRecommendation_shouldReturnFallbackMessage_whenPartsKeyIsMissing() {

        java.util.Map<String, Object> content =
                java.util.Map.of("role", "model");

        java.util.Map<String, Object> candidate =
                java.util.Map.of("content", content);

        java.util.Map<String, Object> responseBody =
                java.util.Map.of("candidates", java.util.List.of(candidate));

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        String result = geminiService.getRecommendation("Suggest Java skills");

        assertEquals(
                "Unable to generate AI recommendation at this time.",
                result
        );
    }

    @Test
    void getRecommendation_shouldReturnFallbackMessage_whenTextKeyIsMissing() {

        java.util.Map<String, Object> part =
                java.util.Map.of("inlineData", "some-data");

        java.util.Map<String, Object> content =
                java.util.Map.of("parts", java.util.List.of(part));

        java.util.Map<String, Object> candidate =
                java.util.Map.of("content", content);

        java.util.Map<String, Object> responseBody =
                java.util.Map.of("candidates", java.util.List.of(candidate));

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        String result = geminiService.getRecommendation("Suggest Java skills");

        assertEquals(
                "Unable to generate AI recommendation at this time.",
                result
        );
    }

    @Test
    void getRecommendation_shouldReturnFallbackMessage_whenResponseBodyIsNull() {

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok()
                        .build();

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        String result = geminiService.getRecommendation("Suggest Java skills");

        assertEquals(
                "Unable to generate AI recommendation at this time.",
                result
        );
    }
    @Test
    void getRecommendation_shouldCallGeminiApiUsingPostMethod() {

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok(
                        java.util.Map.of(
                                "candidates",
                                java.util.List.of(
                                        java.util.Map.of(
                                                "content",
                                                java.util.Map.of(
                                                        "parts",
                                                        java.util.List.of(
                                                                java.util.Map.of(
                                                                        "text",
                                                                        "Keep learning Java"
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        geminiService.getRecommendation("Suggest Java skills");

        org.mockito.Mockito.verify(restTemplate).exchange(
                anyString(),
                org.mockito.ArgumentMatchers.eq(org.springframework.http.HttpMethod.POST),
                any(),
                eq(java.util.Map.class)
        );
    }
    @Test
    void getRecommendation_shouldSetJsonContentType() {

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok(
                        java.util.Map.of(
                                "candidates",
                                java.util.List.of(
                                        java.util.Map.of(
                                                "content",
                                                java.util.Map.of(
                                                        "parts",
                                                        java.util.List.of(
                                                                java.util.Map.of(
                                                                        "text",
                                                                        "Keep learning Java"
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        geminiService.getRecommendation("Suggest Java skills");

        org.mockito.ArgumentCaptor<org.springframework.http.HttpEntity> captor =
                org.mockito.ArgumentCaptor.forClass(
                        org.springframework.http.HttpEntity.class
                );

        org.mockito.Mockito.verify(restTemplate).exchange(
                anyString(),
                any(),
                captor.capture(),
                eq(java.util.Map.class)
        );

        assertEquals(
                org.springframework.http.MediaType.APPLICATION_JSON,
                captor.getValue().getHeaders().getContentType()
        );
    }
    @Test
    void getRecommendation_shouldSendPromptInRequestBody() {

        String prompt = "Suggest Java skills";

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok(
                        java.util.Map.of(
                                "candidates",
                                java.util.List.of(
                                        java.util.Map.of(
                                                "content",
                                                java.util.Map.of(
                                                        "parts",
                                                        java.util.List.of(
                                                                java.util.Map.of(
                                                                        "text",
                                                                        "Keep learning Java"
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        geminiService.getRecommendation(prompt);

        org.mockito.ArgumentCaptor<org.springframework.http.HttpEntity> captor =
                org.mockito.ArgumentCaptor.forClass(
                        org.springframework.http.HttpEntity.class
                );

        org.mockito.Mockito.verify(restTemplate).exchange(
                anyString(),
                any(),
                captor.capture(),
                eq(java.util.Map.class)
        );

        java.util.Map body =
                (java.util.Map) captor.getValue().getBody();

        java.util.List contents =
                (java.util.List) body.get("contents");

        java.util.Map content =
                (java.util.Map) contents.get(0);

        java.util.List parts =
                (java.util.List) content.get("parts");

        java.util.Map part =
                (java.util.Map) parts.get(0);

        assertEquals(prompt, part.get("text"));
    }
    @Test
    void getRecommendation_shouldIncludeApiKeyInUrl() {

        org.springframework.http.ResponseEntity<java.util.Map> response =
                org.springframework.http.ResponseEntity.ok(
                        java.util.Map.of(
                                "candidates",
                                java.util.List.of(
                                        java.util.Map.of(
                                                "content",
                                                java.util.Map.of(
                                                        "parts",
                                                        java.util.List.of(
                                                                java.util.Map.of(
                                                                        "text",
                                                                        "Keep learning Java"
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(java.util.Map.class)
        )).thenReturn(response);

        geminiService.getRecommendation("Suggest Java skills");

        org.mockito.ArgumentCaptor<String> urlCaptor =
                org.mockito.ArgumentCaptor.forClass(String.class);

        org.mockito.Mockito.verify(restTemplate).exchange(
                urlCaptor.capture(),
                any(),
                any(),
                eq(java.util.Map.class)
        );

        assertTrue(urlCaptor.getValue().contains("?key="));
    }
}
