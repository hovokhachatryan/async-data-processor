package com.epam.async_data_processor.api.impl;

import com.epam.async_data_processor.api.TaskController;
import com.epam.async_data_processor.api.dto.CreateTaskRequestDto;
import com.epam.async_data_processor.api.dto.CreateTaskResponseDto;
import com.epam.async_data_processor.api.dto.TaskResponseDto;
import com.epam.async_data_processor.service.TaskOrchestrator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Asynchronous task processing system", description = "Operations related to processing and managing tasks")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskControllerImpl implements TaskController {

    private final TaskOrchestrator taskOrchestrator;

    @Override
    @PostMapping
    public ResponseEntity<CreateTaskResponseDto> submitTask(@Valid @RequestBody CreateTaskRequestDto task) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskOrchestrator.processTask(task));
    }

    @Override
    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return taskOrchestrator.getAllTasks();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskOrchestrator.getTaskById(id));
    }
}
