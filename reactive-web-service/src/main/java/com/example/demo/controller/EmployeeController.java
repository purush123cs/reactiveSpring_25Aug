package com.example.demo.controller;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.EmployeeDepartmentDTO;
import com.example.demo.request.EmployeeCreateRequest;
import com.example.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public Mono<ResponseEntity<EmployeeDTO>> createEmployee(@RequestBody EmployeeCreateRequest employeeCreateRequest) {
        return employeeService.createEmployee(employeeCreateRequest)
                .map(employeeDTO -> ResponseEntity.status(HttpStatus.OK).body(employeeDTO));
    }

    @GetMapping("/{id}")
    private Mono<ResponseEntity<EmployeeDepartmentDTO>> getEmployeeDetails(@PathVariable("id") int employeeId) {
        return employeeService.getEmployeeDetailsById(employeeId)
                .map(employeeDTO -> ResponseEntity.status(HttpStatus.OK).body(employeeDTO));
    }

}
