package com.epam.async_data_processor.api;

import com.epam.async_data_processor.api.dto.CreateTaskRequestDto;
import com.epam.async_data_processor.api.dto.CreateTaskResponseDto;
import com.epam.async_data_processor.api.dto.ErrorResponseDto;
import com.epam.async_data_processor.api.dto.TaskResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

/**
 * Controller interface for managing tasks.
 */
public interface TaskController {

    /**
     * Submits a new task for processing.
     *
     * @param task the task to submit
     * @return the response containing the UUID and status of the submitted task
     */
    @Operation(summary = "Submit a new task for processing",
            responses = {
                    @ApiResponse(description = "Task created successfully", responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateTaskResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })

    ResponseEntity<CreateTaskResponseDto> submitTask(CreateTaskRequestDto task);

    /**
     * Retrieves a list of all submitted tasks.
     *
     * @return a list of all tasks
     */
    @Operation(summary = "Retrieve a list of all submitted tasks",
            responses = {
                    @ApiResponse(description = "Successfully retrieved list of tasks", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    List<TaskResponseDto> getAllTasks();

    /**
     * Retrieves the status of a specific task by its ID.
     *
     * @param id the UUID of the task
     * @return the task with the specified ID
     */
    @Operation(summary = "Retrieve the status of a specific task by its ID",
            responses = {
                    @ApiResponse(description = "Successfully retrieved task", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class))),
                    @ApiResponse(description = "Task not found", responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID id);
}
