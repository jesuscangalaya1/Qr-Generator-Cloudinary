package com.qrgenerator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentRequest {

    private String firstName;
    private String lastName;
    private String email;
    private Integer mobile;

}
