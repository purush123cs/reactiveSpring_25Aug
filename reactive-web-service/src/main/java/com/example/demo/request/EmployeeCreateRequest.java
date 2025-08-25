package com.example.demo.request;

import lombok.Value;
import jakarta.validation.constraints.NotBlank;

@Value
public class EmployeeCreateRequest {
    private String employeeName;
    private String email;
}

