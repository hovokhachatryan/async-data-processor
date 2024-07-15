package com.epam.async_data_processor;

import com.epam.async_data_processor.api.dto.CreateTaskRequestDto;
import com.epam.async_data_processor.api.dto.CreateTaskResponseDto;
import com.epam.async_data_processor.api.dto.TaskResponseDto;
import com.epam.async_data_processor.domain.entity.Task;
import com.epam.async_data_processor.domain.entity.TaskStatus;

import java.util.UUID;

public class TestDataUtils {

    public static Task getTestTask() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setStatus(TaskStatus.PENDING);
        task.setName("testName");
        task.setPayload("testPayload");
        return task;
    }

    public static CreateTaskRequestDto getTestCreateTaskRequestDto() {
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setName("testName");
        createTaskRequestDto.setPayload("testPayload");;
        return createTaskRequestDto;
    }

    public static CreateTaskResponseDto getTestCreateTaskResponseDto(UUID id) {
        CreateTaskResponseDto createTaskResponseDto = new CreateTaskResponseDto();
        createTaskResponseDto.setId(id);
        createTaskResponseDto.setStatus(TaskStatus.PENDING);
        return createTaskResponseDto;
    }

    public static TaskResponseDto getTestTaskResponseDto(UUID id) {
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(id);
        taskResponseDto.setName("testName");
        taskResponseDto.setPayload("testPayload");
        taskResponseDto.setStatus(TaskStatus.PENDING);
        return taskResponseDto;
    }
}
