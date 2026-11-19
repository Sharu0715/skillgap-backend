
package com.sharayu.skillgap.controller;

import com.sharayu.skillgap.dto.SendOtpRequestDto;
import com.sharayu.skillgap.dto.VerifyOtpRequestDto;
import com.sharayu.skillgap.service.BrevoEmailService;
import com.sharayu.skillgap.service.EmailOtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;



import java.util.Map;

@RestController
@RequestMapping("/auth/email")
@RequiredArgsConstructor
public class EmailOtpController {

    private final EmailOtpService emailOtpService;
    private final BrevoEmailService brevoEmailService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(
            @Valid @RequestBody SendOtpRequestDto request) {

        try {
            String otp = emailOtpService.generateOtp(request.getEmail());

            brevoEmailService.sendEmail(
                    request.getEmail(),
                    "SkillGap Email Verification",
                    "Your SkillGap verification OTP is: " + otp
                            + "\n\nThis OTP is valid for 5 minutes."
            );

            return ResponseEntity.ok(
                    Map.of("message", "OTP sent successfully to your email.")
            );

        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", ex.getMessage())
            );
        } catch (RestClientException ex) {
            emailOtpService.removePendingOtp(request.getEmail());

            return ResponseEntity.internalServerError().body(
                    Map.of("message", "Unable to send OTP email. Please try again later.")
            );
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @Valid @RequestBody VerifyOtpRequestDto request) {

        boolean verified = emailOtpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );



        if (!verified) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Invalid or expired OTP.")
            );
        }

        return ResponseEntity.ok(
                Map.of("message", "Email verified successfully.")
        );
    }
}
