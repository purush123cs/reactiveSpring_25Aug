package com.example.demo.service;

import com.example.demo.dto.DepartmentDTO;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.EmployeeDepartmentDTO;
import com.example.demo.request.EmployeeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final @Qualifier("employeeClient") WebClient employeeClient;
    private final @Qualifier("departmentClient") WebClient departmentClient;

    public Mono<EmployeeDTO> createEmployee(EmployeeCreateRequest employeeCreateRequest) {
        return employeeClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeCreateRequest)
                .retrieve()
                .bodyToMono(EmployeeDTO.class)
                .map(employeeDTO -> {
                    employeeDTO.setEmployeeName(employeeDTO.getEmployeeName()+"_modified");
                    return employeeDTO;
                });
    }

    public Mono<EmployeeDepartmentDTO> getEmployeeDetailsById(int employeeId) {
        return employeeClient.get()
                .uri("/{id}", employeeId)
                .retrieve()
                .bodyToMono(EmployeeDTO.class)
                .flatMap(employee -> {
                    String departmentId = employee.getDepartmentId();
                    return departmentClient.get()
                            .uri("/{id}", departmentId)
                            .retrieve()
                            .bodyToMono(DepartmentDTO.class)
                            .map(department -> {
                                EmployeeDepartmentDTO employeeDepartmentDTO = new EmployeeDepartmentDTO();
                                employeeDepartmentDTO.setEmployeeId(employee.getEmployeeId());
                                employeeDepartmentDTO.setEmployeeName(employee.getEmployeeName());
                                employeeDepartmentDTO.setDepartmentName(department.getDepartmentName());
                                return employeeDepartmentDTO;
                            });
                });
    }
}
