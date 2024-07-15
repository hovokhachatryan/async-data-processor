package com.epam.async_data_processor.service;

import com.epam.async_data_processor.domain.entity.Task;

/**
 * Service for processing tasks.
 */
public interface TaskProcessingService {

    /**
     * Processes a task.
     *
     * @param task the task to process
     */
    void processTask(Task task);
}
