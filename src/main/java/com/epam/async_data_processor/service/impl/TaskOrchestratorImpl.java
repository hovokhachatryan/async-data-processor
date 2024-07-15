package com.epam.async_data_processor.service.impl;

import com.epam.async_data_processor.api.dto.CreateTaskRequestDto;
import com.epam.async_data_processor.api.dto.CreateTaskResponseDto;
import com.epam.async_data_processor.api.dto.TaskResponseDto;
import com.epam.async_data_processor.domain.entity.Task;
import com.epam.async_data_processor.domain.entity.TaskStatus;
import com.epam.async_data_processor.exception.TaskCrudException;
import com.epam.async_data_processor.exception.TaskNotFoundException;
import com.epam.async_data_processor.exception.TaskProcessingException;
import com.epam.async_data_processor.service.TaskCrudService;
import com.epam.async_data_processor.service.TaskOrchestrator;
import com.epam.async_data_processor.service.TaskProcessingService;
import com.epam.async_data_processor.service.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskOrchestratorImpl implements TaskOrchestrator {

    private final TaskCrudService taskDataService;
    private final TaskProcessingService taskProcessingService;
    private final TaskMapper taskMapper;

    @Override
    public CreateTaskResponseDto processTask(CreateTaskRequestDto createTaskRequestDto) {
        Instant start = Instant.now();
        try {

            Task task = taskMapper.toEntity(createTaskRequestDto);

            // Save task with PENDING status
            task.setStatus(TaskStatus.PENDING);
            Task savedTask = taskDataService.saveTask(task);

            CreateTaskResponseDto createTaskResponseDto = taskMapper.toCreateTaskResponseDto(savedTask);

            // Process task asynchronously
            taskProcessingService.processTask(savedTask);

            return createTaskResponseDto;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TaskProcessingException("Error orchestrating task", exception);
        } finally {
            Instant end = Instant.now();
            log.info(String.format("orchestrateTask service logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
            return taskDataService.getAllTasks().stream()
                    .map(taskMapper::toDto)
                    .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDto getTaskById(UUID id) {
            return taskDataService.getTaskById(id)
                    .map(taskMapper::toDto)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }
}
