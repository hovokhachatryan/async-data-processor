package com.epam.async_data_processor.api;

import com.epam.async_data_processor.TestDataUtils;
import com.epam.async_data_processor.api.dto.CreateTaskRequestDto;
import com.epam.async_data_processor.api.dto.CreateTaskResponseDto;
import com.epam.async_data_processor.api.dto.TaskResponseDto;
import com.epam.async_data_processor.api.impl.TaskControllerImpl;
import com.epam.async_data_processor.domain.entity.TaskStatus;
import com.epam.async_data_processor.service.TaskOrchestrator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerImplTest {

    @Mock
    private TaskOrchestrator taskOrchestrator;

    @InjectMocks
    private TaskControllerImpl taskController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private UUID taskId;
    private TaskResponseDto taskResponseDto;
    private CreateTaskRequestDto createTaskRequestDto;
    private CreateTaskResponseDto createTaskResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();
        taskId = UUID.randomUUID();
        createTaskRequestDto = TestDataUtils.getTestCreateTaskRequestDto();
        createTaskResponseDto = TestDataUtils.getTestCreateTaskResponseDto(taskId);
        taskResponseDto = TestDataUtils.getTestTaskResponseDto(taskId);
    }

    @Test
    void submitTask_shouldReturnCreatedTaskResponse() throws Exception {
        when(taskOrchestrator.processTask(any(CreateTaskRequestDto.class))).thenReturn(createTaskResponseDto);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.status").value(TaskStatus.PENDING.toString()));

        verify(taskOrchestrator, times(1)).processTask(any(CreateTaskRequestDto.class));
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() throws Exception {
        List<TaskResponseDto> tasks = Arrays.asList(taskResponseDto);
        when(taskOrchestrator.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(tasks.size()))
                .andExpect(jsonPath("$[0].id").value(taskId.toString()))
                .andExpect(jsonPath("$[0].name").value("testName"))
                .andExpect(jsonPath("$[0].payload").value("testPayload"))
                .andExpect(jsonPath("$[0].status").value(TaskStatus.PENDING.toString()));

        verify(taskOrchestrator, times(1)).getAllTasks();
    }

    @Test
    void getTaskById_shouldReturnTaskWhenFound() throws Exception {
        when(taskOrchestrator.getTaskById(taskId)).thenReturn(taskResponseDto);

        mockMvc.perform(get("/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.payload").value("testPayload"))
                .andExpect(jsonPath("$.status").value(TaskStatus.PENDING.toString()));

        verify(taskOrchestrator, times(1)).getTaskById(taskId);
    }
}
