package com.epam.async_data_processor.service;

import com.epam.async_data_processor.TestDataUtils;
import com.epam.async_data_processor.domain.entity.Task;
import com.epam.async_data_processor.domain.entity.TaskStatus;
import com.epam.async_data_processor.domain.repository.TaskRepository;
import com.epam.async_data_processor.exception.TaskCrudException;
import com.epam.async_data_processor.service.impl.TaskCrudServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskCrudServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskCrudServiceImpl taskCrudService;

    private Task task = TestDataUtils.getTestTask();

    private UUID taskId = task.getId();

    @BeforeEach
    void setUp() {
        task = TestDataUtils.getTestTask();
        taskId = task.getId();
    }
    @Test
    void saveTask_shouldSaveTaskSuccessfully() {

        when(taskRepository.save(task)).thenReturn(task);
        Task savedTask = taskCrudService.saveTask(task);

        verify(taskRepository, times(1)).save(task);
        assertEquals(task, savedTask);
    }

    @Test
    void saveTask_shouldThrowTaskCrudExceptionOnSaveFailure() {
        when(taskRepository.save(task)).thenThrow(new RuntimeException("Save failed"));

        Exception exception = assertThrows(TaskCrudException.class, () -> {
            taskCrudService.saveTask(task);
        });

        verify(taskRepository, times(1)).save(task);
        assertEquals("Error saving task", exception.getMessage());
    }

    @Test
    void updateTaskStatus_shouldUpdateTaskStatusSuccessfully() {
        when(taskRepository.save(task)).thenReturn(task);

        taskCrudService.updateTaskStatus(task, TaskStatus.PROCESSING);

        verify(taskRepository, times(1)).save(task);
        assertEquals(TaskStatus.PROCESSING, task.getStatus());
    }

    @Test
    void updateTaskStatus_shouldThrowTaskCrudExceptionOnUpdateFailure() {
        when(taskRepository.save(task)).thenThrow(new RuntimeException("Update failed"));

        Exception exception = assertThrows(TaskCrudException.class, () -> {
            taskCrudService.updateTaskStatus(task, TaskStatus.PROCESSING);
        });

        verify(taskRepository, times(1)).save(task);
        assertEquals("Error updating task status", exception.getMessage());
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() {
        List<Task> tasks = Arrays.asList(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskCrudService.getAllTasks();

        verify(taskRepository, times(1)).findAll();
        assertEquals(tasks, result);
    }

    @Test
    void getAllTasks_shouldThrowTaskCrudExceptionOnRetrievalFailure() {
        when(taskRepository.findAll()).thenThrow(new RuntimeException("Find all failed"));

        Exception exception = assertThrows(TaskCrudException.class, () -> {
            taskCrudService.getAllTasks();
        });

        verify(taskRepository, times(1)).findAll();
        assertEquals("Error retrieving all tasks", exception.getMessage());
    }

    @Test
    void getTaskById_shouldReturnTaskWhenFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Optional<Task> result = taskCrudService.getTaskById(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }

    @Test
    void getTaskById_shouldThrowTaskCrudExceptionOnRetrievalFailure() {
        when(taskRepository.findById(taskId)).thenThrow(new RuntimeException("Find by ID failed"));

        Exception exception = assertThrows(TaskCrudException.class, () -> {
            taskCrudService.getTaskById(taskId);
        });

        verify(taskRepository, times(1)).findById(taskId);
        assertEquals("Error retrieving task by ID", exception.getMessage());
    }

    @Test
    void getTaskById_shouldReturnEmptyWhenTaskNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Optional<Task> result = taskCrudService.getTaskById(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        assertFalse(result.isPresent());
    }

}
