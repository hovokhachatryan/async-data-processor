package com.epam.async_data_processor.api.dto;

public record ErrorResponseDto(
        String title,
        String description,
        int statusCode,
        String resourceUrl
) {}
