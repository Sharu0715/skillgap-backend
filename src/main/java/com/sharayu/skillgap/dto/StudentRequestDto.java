package com.sharayu.skillgap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentRequestDto {
    private String name;
    private String email;
    private String password;


}