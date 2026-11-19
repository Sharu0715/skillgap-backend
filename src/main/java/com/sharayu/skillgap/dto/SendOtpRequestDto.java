package com.sharayu.skillgap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOtpRequestDto {

    @NotBlank
    @Email
    private String email;
}