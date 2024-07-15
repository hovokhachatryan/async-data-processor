package com.epam.async_data_processor.service;

import com.epam.async_data_processor.config.TaskExecutorConfiguration;
import com.epam.async_data_processor.domain.entity.Task;
import com.epam.async_data_processor.domain.entity.TaskStatus;
import com.epam.async_data_processor.service.impl.TaskProcessingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.epam.async_data_processor.TestDataUtils.getTestTask;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TaskProcessingServiceImplTest {

    private static final long TEST_TIMOUT_MS = 10000L;
    private static final long TEST_FAKE_EXECUTION_TIME__MS = 1000L;
    private static final int TEST_EXECUTOR_POOL_SIZE = 2;

    @Mock
    private TaskCrudService taskCrudService;

    private TaskExecutorConfiguration taskExecutorConfiguration;
    private TaskProcessingServiceImpl taskProcessingService;
    private ExecutorService executorService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the real executor service and task executor configuration
        taskExecutorConfiguration = new TaskExecutorConfiguration(TEST_TIMOUT_MS, TEST_FAKE_EXECUTION_TIME__MS, TEST_EXECUTOR_POOL_SIZE);
        executorService = taskExecutorConfiguration.getExecutorService();

        taskProcessingService = new TaskProcessingServiceImpl(taskCrudService, taskExecutorConfiguration);
    }

    @AfterEach
    void tearDown() {
        executorService.shutdown();
    }

    @Test
    void processTask_shouldProcessTaskSuccessfully() throws Exception {
        Task task = getTestTask();

        taskProcessingService.processTask(task);

        // Wait a bit to let async processing complete
        executorService.awaitTermination(TEST_FAKE_EXECUTION_TIME__MS, TimeUnit.MILLISECONDS);

        verify(taskCrudService).updateTaskStatus(task, TaskStatus.PROCESSING);
        verify(taskCrudService).updateTaskStatus(task, TaskStatus.COMPLETED);
    }

    @Test
    void processTask_shouldHandleException() throws Exception {
        Task task = getTestTask();

        doThrow(new RuntimeException("Test Exception")).when(taskCrudService).updateTaskStatus(task, TaskStatus.PROCESSING);

        taskProcessingService.processTask(task);

        // Wait a bit to let async processing complete
        executorService.awaitTermination(TEST_FAKE_EXECUTION_TIME__MS, TimeUnit.MILLISECONDS);

        verify(taskCrudService).updateTaskStatus(task, TaskStatus.PROCESSING);
        verify(taskCrudService).updateTaskStatus(task, TaskStatus.FAILED);
    }


    @Test
    void processTask_shouldHandleTimeout() throws Exception {
        Task task = getTestTask();

        // Adjust the fake execution time to simulate a timeout
        taskExecutorConfiguration = new TaskExecutorConfiguration(1000L, 3000L, 2);
        taskProcessingService = new TaskProcessingServiceImpl(taskCrudService, taskExecutorConfiguration);

        taskProcessingService.processTask(task);

        // Wait a bit to let async processing complete
        executorService.awaitTermination(3000L, TimeUnit.MILLISECONDS);

        verify(taskCrudService).updateTaskStatus(task, TaskStatus.PROCESSING);
        verify(taskCrudService).updateTaskStatus(task, TaskStatus.FAILED);
    }
}
