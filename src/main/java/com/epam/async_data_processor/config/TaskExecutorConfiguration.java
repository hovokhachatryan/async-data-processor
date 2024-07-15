package com.epam.async_data_processor.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Getter
public class TaskExecutorConfiguration {

    private final long timeoutMs;
    private final long fakeExecutionTimeMs;
    private final int executorPoolSize;
    private final ExecutorService executorService;

    public TaskExecutorConfiguration(@Value("${task.executor.timeoutMs}") long timeoutMs,
                                     @Value("${task.executor.fakeExecutionTimeMs}") long fakeExecutionTimeMs,
                                     @Value("${task.executor.poolSize}") int executorPoolSize) {
        this.timeoutMs = timeoutMs;
        this.fakeExecutionTimeMs = fakeExecutionTimeMs;
        this.executorPoolSize = executorPoolSize;
        this.executorService = Executors.newFixedThreadPool(executorPoolSize);
    }
}
