package it.unipi.chesspuzzles.client.shared;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class Attemp {
    public Integer id;
    public Integer userId;
    public String timestamp;
    public TYPE type;

    public Attemp(Integer userId, LocalDateTime timestamp, TYPE type) {
        this.userId = userId;
        this.timestamp = timestamp.toString();
        this.type = type;
    }

    // Default constructor is required by gson
    public Attemp() {}

    public LocalDateTime getParsedTimestamp() {
        return LocalDateTime.parse(timestamp);
    }

    public enum TYPE {
        SOLVE,
        HINT,
        FAIL
    }
}
