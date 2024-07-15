package com.epam.async_data_processor.api.dto;

import com.epam.async_data_processor.domain.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreateTaskResponseDto {

    private UUID id;

    private TaskStatus status;

}