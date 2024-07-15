package com.epam.async_data_processor.exception;

public class TaskProcessingException extends RuntimeException {

    public TaskProcessingException(String message, Throwable cause) {
        super(message);
    }
}
