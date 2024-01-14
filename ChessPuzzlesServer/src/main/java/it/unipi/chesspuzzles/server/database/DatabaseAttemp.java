package it.unipi.chesspuzzles.server.database;

import it.unipi.chesspuzzles.server.shared.Attemp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Entity
@Table(name = "attemp", schema = "635502")
public class DatabaseAttemp implements Serializable {
// Implementation of Serializable interface is not strictly needed but reccomended

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "type", length = 5, nullable = false)
    private String type;

    public DatabaseAttemp(Attemp attemp) {
        this.userId = attemp.userId;
        this.timestamp = LocalDateTime.parse(attemp.timestamp);
        this.type = String.valueOf(attemp.type);
    }

    // Default constructor is required by jpa ...
    public DatabaseAttemp() {}

    // ... and so are getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
