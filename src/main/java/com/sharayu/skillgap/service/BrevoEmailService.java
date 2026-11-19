
package com.sharayu.skillgap.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailService {

    @Value("${brevo.api.url}")
    private String apiUrl;

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private final RestClient restClient;

    public BrevoEmailService() {
        this.restClient = RestClient.create();
    }

    public void sendEmail(String toEmail, String subject, String content) {

        Map<String, Object> sender = Map.of(
                "name", senderName,
                "email", senderEmail
        );

        Map<String, String> recipient = Map.of(
                "email", toEmail
        );

        Map<String, Object> requestBody = Map.of(
                "sender", sender,
                "to", List.of(recipient),
                "subject", subject,
                "textContent", content
        );

        restClient.post()
                .uri(apiUrl)
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }
}
