package com.epam.async_data_processor.service;

import com.epam.async_data_processor.domain.entity.Task;
import com.epam.async_data_processor.domain.entity.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing tasks.
 */
public interface TaskCrudService {

    /**
     * Saves a task.
     *
     * @param task the task to save
     * @return the saved task
     */
    Task saveTask(Task task);

    /**
     * Updates the status of a task.
     *
     * @param task the task to update
     * @param status the new status of the task
     */
    void updateTaskStatus(Task task, TaskStatus status);

    /**
     * Retrieves all tasks.
     *
     * @return a list of all tasks
     */
    List<Task> getAllTasks();

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task
     * @return an optional containing the task if found, or empty if not found
     */
    Optional<Task> getTaskById(UUID id);
}
