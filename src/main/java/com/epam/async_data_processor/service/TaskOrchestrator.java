package com.epam.async_data_processor.service;

import com.epam.async_data_processor.api.dto.CreateTaskRequestDto;
import com.epam.async_data_processor.api.dto.CreateTaskResponseDto;
import com.epam.async_data_processor.api.dto.TaskResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Service for orchestrating task processing and CRUD operations.
 */
public interface TaskOrchestrator {

    /**
     * Orchestrates the processing of a task.
     *
     * @param task the task to process
     * @return the saved task details
     */
    CreateTaskResponseDto processTask(CreateTaskRequestDto task);

    /**
     * Retrieves all tasks.
     *
     * @return a list of all tasks
     */
    List<TaskResponseDto> getAllTasks();

    /**
     * Retrieves a task by ID.
     *
     * @param id the UUID of the task
     * @return an optional containing the task if found, or empty if not found
     */
    TaskResponseDto getTaskById(UUID id);
}
