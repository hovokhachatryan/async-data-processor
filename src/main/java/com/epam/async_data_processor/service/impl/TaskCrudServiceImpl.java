package com.epam.async_data_processor.service.impl;

import com.epam.async_data_processor.domain.entity.Task;
import com.epam.async_data_processor.domain.entity.TaskStatus;
import com.epam.async_data_processor.domain.repository.TaskRepository;
import com.epam.async_data_processor.exception.TaskCrudException;
import com.epam.async_data_processor.service.TaskCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskCrudServiceImpl implements TaskCrudService {

    private final TaskRepository taskRepository;

    @Override
    public Task saveTask(Task task) {
        Instant start = Instant.now();
        try {
            return taskRepository.save(task);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TaskCrudException("Error saving task", exception);
        } finally {
            Instant end = Instant.now();
            log.info(String.format("saveTask method logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }

    @Override
    public void updateTaskStatus(Task task, TaskStatus status) {
        Instant start = Instant.now();
        try {
            task.setStatus(status);
            taskRepository.save(task);
            log.info(String.format("Status for task with id %s updated to %s", task.getId(), status));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TaskCrudException("Error updating task status", exception);
        } finally {
            Instant end = Instant.now();
            log.info(String.format("updateTaskStatus method logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }

    @Override
    public List<Task> getAllTasks() {
        Instant start = Instant.now();
        try {
            return taskRepository.findAll();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TaskCrudException("Error retrieving all tasks", exception);
        } finally {
            Instant end = Instant.now();
            log.info(String.format("getAllTasks method logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }

    @Override
    public Optional<Task> getTaskById(UUID id) {
        Instant start = Instant.now();
        try {
            return taskRepository.findById(id);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TaskCrudException("Error retrieving task by ID", exception);
        } finally {
            Instant end = Instant.now();
            log.info(String.format("getTaskById method logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }
}
