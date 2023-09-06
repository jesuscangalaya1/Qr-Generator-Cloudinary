package com.qrgenerator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer mobile;
    private String url;
}
