package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
public class EmployeeDTO {
    private String employeeId;
    private String employeeName;
    private String departmentId;
}
