package com.epam.async_data_processor.domain.repository;

import com.epam.async_data_processor.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
