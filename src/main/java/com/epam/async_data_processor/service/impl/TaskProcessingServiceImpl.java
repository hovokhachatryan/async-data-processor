package com.epam.async_data_processor.service.impl;

import com.epam.async_data_processor.config.TaskExecutorConfiguration;
import com.epam.async_data_processor.domain.entity.Task;
import com.epam.async_data_processor.domain.entity.TaskStatus;
import com.epam.async_data_processor.service.TaskCrudService;
import com.epam.async_data_processor.service.TaskProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProcessingServiceImpl implements TaskProcessingService {

    private final TaskCrudService taskCrudService;
    private final TaskExecutorConfiguration taskExecutorConfiguration;

    @Override
    public void processTask(Task task) {
        Instant start = Instant.now();

        CompletableFuture.runAsync(() -> processTaskAsync(task), taskExecutorConfiguration.getExecutorService())
                .orTimeout(taskExecutorConfiguration.getTimeoutMs(), TimeUnit.MILLISECONDS)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        handleException(task, ex);
                    } else {
                        taskCrudService.updateTaskStatus(task, TaskStatus.COMPLETED);
                    }
                });

        Instant end = Instant.now();
        log.info("processTask service logic finished. [durationMs = {}]", Duration.between(start, end).toMillis());
    }

    private void processTaskAsync(Task task) {
        try {
            taskCrudService.updateTaskStatus(task, TaskStatus.PROCESSING);

            // Simulate time-consuming task
            Thread.sleep(taskExecutorConfiguration.getFakeExecutionTimeMs());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            taskCrudService.updateTaskStatus(task, TaskStatus.FAILED);
            log.error("Task was interrupted: {}", e.getMessage(), e);
        } catch (Exception e) {
            taskCrudService.updateTaskStatus(task, TaskStatus.FAILED);
            log.error("Task failed: {}", e.getMessage(), e);
        }
    }

    private void handleException(Task task, Throwable ex) {
        log.error("Task timed out: {}", ex.getMessage(), ex);
        taskCrudService.updateTaskStatus(task, TaskStatus.FAILED);
    }
}
