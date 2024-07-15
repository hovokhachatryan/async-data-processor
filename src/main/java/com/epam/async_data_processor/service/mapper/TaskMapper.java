package com.epam.async_data_processor.service.mapper;

import com.epam.async_data_processor.api.dto.CreateTaskRequestDto;
import com.epam.async_data_processor.api.dto.CreateTaskResponseDto;
import com.epam.async_data_processor.api.dto.TaskResponseDto;
import com.epam.async_data_processor.domain.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponseDto toDto(Task task);
    CreateTaskResponseDto toCreateTaskResponseDto(Task task);
    Task toEntity(CreateTaskRequestDto createTaskRequestDto);
}
