package com.epam.async_data_processor.service.mapper;

import com.epam.async_data_processor.TestDataUtils;
import com.epam.async_data_processor.api.dto.CreateTaskRequestDto;
import com.epam.async_data_processor.api.dto.CreateTaskResponseDto;
import com.epam.async_data_processor.api.dto.TaskResponseDto;
import com.epam.async_data_processor.domain.entity.Task;
import com.epam.async_data_processor.domain.entity.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskMapperTest {

    private TaskMapper taskMapper;

    private UUID taskId;
    private Task task;
    private CreateTaskRequestDto createTaskRequestDto;
    private CreateTaskResponseDto createTaskResponseDto;
    private TaskResponseDto taskResponseDto;

    @BeforeEach
    void setUp() {
        taskMapper = Mappers.getMapper(TaskMapper.class);
        task = TestDataUtils.getTestTask();
        taskId = task.getId();
        createTaskRequestDto = TestDataUtils.getTestCreateTaskRequestDto();
        createTaskResponseDto = TestDataUtils.getTestCreateTaskResponseDto(taskId);
        taskResponseDto = TestDataUtils.getTestTaskResponseDto(taskId);
    }

    @Test
    void toDto_shouldMapTaskToTaskResponseDto() {
        TaskResponseDto dto = taskMapper.toDto(task);

        assertNotNull(dto);
        assertEquals(task.getId(), dto.getId());
        assertEquals(task.getName(), dto.getName());
        assertEquals(task.getPayload(), dto.getPayload());
        assertEquals(task.getStatus(), dto.getStatus());
    }

    @Test
    void toCreateTaskResponseDto_shouldMapTaskToCreateTaskResponseDto() {
        CreateTaskResponseDto dto = taskMapper.toCreateTaskResponseDto(task);

        assertNotNull(dto);
        assertEquals(task.getId(), dto.getId());
        assertEquals(task.getStatus(), dto.getStatus());
    }

    @Test
    void toEntity_shouldMapCreateTaskRequestDtoToTask() {
        Task entity = taskMapper.toEntity(createTaskRequestDto);

        assertNotNull(entity);
        assertEquals(createTaskRequestDto.getName(), entity.getName());
        assertEquals(createTaskRequestDto.getPayload(), entity.getPayload());
    }
}
