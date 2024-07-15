package com.epam.async_data_processor.exception;

public class TaskCrudException extends RuntimeException {
    public TaskCrudException(String message, Throwable cause) {
        super(message, cause);
    }
}
