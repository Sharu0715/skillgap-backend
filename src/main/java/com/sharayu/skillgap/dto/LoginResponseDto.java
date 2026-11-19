
package com.sharayu.skillgap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private Long studentId;
    private Long adminId;
    private String name;
    private String role;
}