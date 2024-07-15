package com.epam.async_data_processor.service;

import com.epam.async_data_processor.TestDataUtils;
import com.epam.async_data_processor.api.dto.CreateTaskRequestDto;
import com.epam.async_data_processor.api.dto.CreateTaskResponseDto;
import com.epam.async_data_processor.api.dto.TaskResponseDto;
import com.epam.async_data_processor.domain.entity.Task;
import com.epam.async_data_processor.exception.TaskCrudException;
import com.epam.async_data_processor.exception.TaskNotFoundException;
import com.epam.async_data_processor.exception.TaskProcessingException;
import com.epam.async_data_processor.service.impl.TaskOrchestratorImpl;
import com.epam.async_data_processor.service.mapper.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskOrchestratorImplTest {

    @Mock
    private TaskCrudService taskCrudService;

    @Mock
    private TaskProcessingService taskProcessingService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskOrchestratorImpl taskOrchestrator;

    private UUID taskId;
    private Task task;
    private CreateTaskRequestDto createTaskRequestDto;
    private CreateTaskResponseDto createTaskResponseDto;
    private TaskResponseDto taskResponseDto;

    @BeforeEach
    void setUp() {
        task = TestDataUtils.getTestTask();
        taskId = task.getId();
        createTaskRequestDto = TestDataUtils.getTestCreateTaskRequestDto();
        createTaskResponseDto = TestDataUtils.getTestCreateTaskResponseDto(taskId);
        taskResponseDto = TestDataUtils.getTestTaskResponseDto(taskId);
    }

    @Test
    void processTask_shouldProcessTaskSuccessfully() {
        when(taskMapper.toEntity(createTaskRequestDto)).thenReturn(task);
        when(taskCrudService.saveTask(task)).thenReturn(task);
        when(taskMapper.toCreateTaskResponseDto(task)).thenReturn(createTaskResponseDto);

        CreateTaskResponseDto response = taskOrchestrator.processTask(createTaskRequestDto);

        verify(taskCrudService, times(1)).saveTask(task);
        verify(taskProcessingService, times(1)).processTask(task);
        assertEquals(createTaskResponseDto, response);
    }

    @Test
    void processTask_shouldThrowTaskProcessingExceptionOnFailure() {
        when(taskMapper.toEntity(createTaskRequestDto)).thenReturn(task);
        when(taskCrudService.saveTask(task)).thenThrow(new RuntimeException("Save failed"));

        Exception exception = assertThrows(TaskProcessingException.class, () -> {
            taskOrchestrator.processTask(createTaskRequestDto);
        });

        verify(taskCrudService, times(1)).saveTask(task);
        verify(taskProcessingService, times(0)).processTask(task);
        assertEquals("Error orchestrating task", exception.getMessage());
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() {
        List<Task> tasks = Arrays.asList(task);
        when(taskCrudService.getAllTasks()).thenReturn(tasks);
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);

        List<TaskResponseDto> result = taskOrchestrator.getAllTasks();

        verify(taskCrudService, times(1)).getAllTasks();
        verify(taskMapper, times(1)).toDto(task);
        assertEquals(1, result.size());
        assertEquals(taskResponseDto, result.get(0));
    }
    @Test
    void getTaskById_shouldReturnTaskWhenFound() {
        when(taskCrudService.getTaskById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskResponseDto);

        TaskResponseDto result = taskOrchestrator.getTaskById(taskId);

        verify(taskCrudService, times(1)).getTaskById(taskId);
        verify(taskMapper, times(1)).toDto(task);
        assertEquals(taskResponseDto, result);
    }

    @Test
    void getTaskById_shouldThrowTaskNotFoundExceptionWhenTaskNotFound() {
        when(taskCrudService.getTaskById(taskId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskOrchestrator.getTaskById(taskId);
        });

        verify(taskCrudService, times(1)).getTaskById(taskId);
        verify(taskMapper, times(0)).toDto(any(Task.class));
        assertEquals("Task not found with id: " + taskId, exception.getMessage());
    }

}
