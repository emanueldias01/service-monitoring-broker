package br.com.emanueldias.Log.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "log-message")
public class LogMessageEntity {
    @Id
    private String id;
    private LocalDateTime timestamp;
    private String level;
    private String operation;

    public LogMessageEntity() {

    }

    public LogMessageEntity(String level, String operation, LocalDateTime timestamp) {
        this.timestamp = timestamp;
        this.level = level;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", level='" + level + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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