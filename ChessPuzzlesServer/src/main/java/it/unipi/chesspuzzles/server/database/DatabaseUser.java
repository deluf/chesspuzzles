package it.unipi.chesspuzzles.server.database;

import it.unipi.chesspuzzles.server.shared.User;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("unused")
@Entity
@Table(name = "user", schema = "635502")
public class DatabaseUser implements Serializable {
// Implementation of Serializable interface is not strictly needed but reccomended

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public DatabaseUser(User user) {
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
    }

    // Default constructor is required by jpa ...
    public DatabaseUser() {}

    // ... and so are getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

