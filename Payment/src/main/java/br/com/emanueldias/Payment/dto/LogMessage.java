package br.com.emanueldias.Payment.dto;

import java.time.LocalDateTime;

public class LogMessage {
    private LocalDateTime timestamp;
    private String level;
    private String operation;

    public LogMessage(String level, String operation) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.operation = operation;
    }
}
