package com.inditex.hiring.controller.exception;

import java.time.Instant;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException(Instant startDate, Instant endDate) {
        super(String.format("Invalid date for start date (%s) or end date (%s)", startDate, endDate));
    }
}
