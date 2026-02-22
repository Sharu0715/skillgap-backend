package com.sharayu.skillgap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentResponseDto {
    private Long id;
    private String name;
    private String email;


}
