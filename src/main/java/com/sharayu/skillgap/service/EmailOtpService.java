
package com.sharayu.skillgap.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailOtpService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int RESEND_COOLDOWN_SECONDS = 60;

    private final SecureRandom secureRandom = new SecureRandom();

    private final ConcurrentHashMap<String, OtpData> otpStorage =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Instant> verifiedEmails =
            new ConcurrentHashMap<>();

    public String generateOtp(String email) {

        String normalizedEmail = normalizeEmail(email);
        Instant now = Instant.now();

        OtpData existingOtp = otpStorage.get(normalizedEmail);

        if (existingOtp != null
                && now.isBefore(existingOtp.getLastSentAt()
                .plusSeconds(RESEND_COOLDOWN_SECONDS))) {

            throw new IllegalStateException(
                    "Please wait 60 seconds before requesting another OTP."
            );
        }

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        OtpData otpData = new OtpData(
                otp,
                now.plusSeconds(OTP_EXPIRY_MINUTES * 60L),
                now
        );

        verifiedEmails.remove(normalizedEmail);
        otpStorage.put(normalizedEmail, otpData);

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {

        String normalizedEmail = normalizeEmail(email);
        OtpData otpData = otpStorage.get(normalizedEmail);

        if (otpData == null) {
            return false;
        }

        if (!Instant.now().isBefore(otpData.getExpiresAt())) {
            otpStorage.remove(normalizedEmail, otpData);
            return false;
        }

        if (!otpData.getOtp().equals(otp)) {
            return false;
        }

        verifiedEmails.put(normalizedEmail, otpData.getExpiresAt());
        otpStorage.remove(normalizedEmail, otpData);

        return true;
    }

    public boolean isEmailVerified(String email) {

        String normalizedEmail = normalizeEmail(email);
        Instant expiresAt = verifiedEmails.get(normalizedEmail);

        if (expiresAt == null) {
            return false;
        }

        if (!Instant.now().isBefore(expiresAt)) {
            verifiedEmails.remove(normalizedEmail, expiresAt);
            return false;
        }

        return true;
    }

    public void removeOtp(String email) {

        String normalizedEmail = normalizeEmail(email);

        otpStorage.remove(normalizedEmail);
        verifiedEmails.remove(normalizedEmail);
    }

    public void removePendingOtp(String email) {
        otpStorage.remove(normalizeEmail(email));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static class OtpData {

        private final String otp;
        private final Instant expiresAt;
        private final Instant lastSentAt;

        public OtpData(
                String otp,
                Instant expiresAt,
                Instant lastSentAt
        ) {
            this.otp = otp;
            this.expiresAt = expiresAt;
            this.lastSentAt = lastSentAt;
        }

        public String getOtp() {
            return otp;
        }

        public Instant getExpiresAt() {
            return expiresAt;
        }

        public Instant getLastSentAt() {
            return lastSentAt;
        }
    }
}
