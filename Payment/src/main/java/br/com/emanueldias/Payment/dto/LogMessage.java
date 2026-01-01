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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
